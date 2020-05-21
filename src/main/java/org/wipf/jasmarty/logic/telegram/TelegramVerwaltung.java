package org.wipf.jasmarty.logic.telegram;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.MsqlLite;
import org.wipf.jasmarty.logic.base.QMain;
import org.wipf.jasmarty.logic.base.Wipf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mashape.unirest.http.Unirest;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("MTelegram");

	private Integer FailCountTelegram;
	private Integer TelegramOffsetID;
	private String BOTKEY;

	@Inject
	Wipf wipf;

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telegramlog (msgid INTEGER, msg TEXT, antw TEXT, chatid INTEGER, msgfrom TEXT, msgdate INTEGER, type TEXT);");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemsg (id integer primary key autoincrement, request TEXT, response TEXT, options TEXT, editby TEXT, date INTEGER);");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemotd (id integer primary key autoincrement, text TEXT, editby TEXT, date INTEGER);");

		} catch (Exception e) {
			LOGGER.warn("initDB Telegram " + e);
		}
	}

	/**
	 * 
	 */
	public boolean loadConfig() {
		// Auf 0 setzen -> definierter zustand
		this.TelegramOffsetID = 0;
		// Load bot config
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT val FROM settings WHERE id = 'telegrambot';");
			this.BOTKEY = (rs.getString("val"));
			rs.close();
			return true;
		} catch (Exception e) {
			LOGGER.warn("telegrambot nicht in db gefunden."
					+ " Setzen mit 'curl -X POST localhost:8080/setbot/bot2343242:ABCDEF348590247354352343345'");
			return false;
		}
	}

	/**
	 * @param sBot
	 * @return
	 */
	public Boolean setbot(String sBot) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM settings WHERE id = 'telegrambot'");
			stmt.execute("INSERT INTO settings (id, val) VALUES ('telegrambot','" + sBot + "')");
			this.BOTKEY = sBot;
			LOGGER.info("Bot Key: " + this.BOTKEY);

			return true;
		} catch (Exception e) {
			LOGGER.warn("setbot " + e);
			return false;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public void sendToTelegram(Telegram t) {
		try {
			String sAntwort = t.getAntwort();
			if (sAntwort == null || sAntwort.equals("")) {
				sAntwort = "Leere%20Antwort";
			}

			String sResJson = Unirest.post("https://api.telegram.org/" + this.BOTKEY + "/sendMessage?chat_id="
					+ t.getChatID() + "&text=" + sAntwort).asString().getBody();

			// parse josn
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(sResJson);

			if (!jn.get("ok").asBoolean()) {
				LOGGER.warn("API fail:" + sResJson + " Antworttext: '" + sAntwort + "'");
			}

		} catch (Exception e) {
			LOGGER.warn("Telegram senden " + e);
		}
	}

	/**
	 * 
	 */
	public void readUpdateFromTelegram() {
		try {
			String sJson;
			if (this.TelegramOffsetID == 0) {
				sJson = Unirest.post("https://api.telegram.org/" + this.BOTKEY + "/getUpdates").asString().getBody();
			} else {
				sJson = Unirest
						.post("https://api.telegram.org/" + this.BOTKEY + "/getUpdates?offset=" + this.TelegramOffsetID)
						.asString().getBody();
			}
			// parse josn
			ObjectMapper mapper = new ObjectMapper();
			ArrayList<Telegram> li = new ArrayList<>();

			JsonNode jn = mapper.readTree(sJson);

			if (!jn.get("ok").asBoolean()) {
				LOGGER.warn("API fail:" + sJson);
				this.FailCountTelegram++;
				return;
			}

			for (JsonNode n : jn) {
				for (JsonNode nn : n) {
					Telegram t = new Telegram();
					try {
						// Nachricht gelesen -> löschen am Telegram server
						this.TelegramOffsetID = nn.get("update_id").asInt() + 1;
						JsonNode msg = nn.get("message");
						t.setMid(msg.get("message_id").asInt());
						t.setMessage(msg.get("text").asText());
						t.setChatID(msg.get("chat").get("id").asInt());
						t.setType(msg.get("chat").get("type").asText());
						t.setDate(msg.get("date").asInt());
						t.setFrom(msg.get("from").toString());
						li.add(t);
					} catch (Exception e) {
						// weiter da sticker oder ähnliches
					}
				}
			}

			// Maximal 5 MSG abarbeiten
			if (li.size() > 5) {
				this.TelegramOffsetID = this.TelegramOffsetID - li.size() + 5;
			}

			// ids zu db
			Integer nMax = 0;
			for (Telegram t : li) {
				nMax++;
				if (nMax <= 5) {
					try {
						t.setAntwort(menueMsg(t));
						saveTelegramToDB(t);
						sendToTelegram(t);
					} catch (Exception e) {
						LOGGER.warn("bearbeiteMsg " + e);
					}
				}
			}
			if (this.FailCountTelegram != 0) {
				// Wenn Telegram wieder erreichbar ist
				sendExtIp();
			}
			this.FailCountTelegram = 0;

		} catch (

		Exception e) {
			this.FailCountTelegram++;
			LOGGER.warn("readUpdateFromTelegram fails: " + this.FailCountTelegram + " " + e);
		}
	}

	/**
	 * @return
	 */
	public String contSend() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telegramlog;");
			return rs.getString("COUNT(*)") + " Nachrichten gesendet";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return null;
		}
	}

	/**
	 * @param t
	 */
	public void saveTelegramToDB(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT INTO telegramlog (msgid, msg, antw, chatid, msgfrom, msgdate, type)" + " VALUES ('"
					+ t.getMid() + "','" + t.getMessage() + "','" + t.getAntwort() + "','" + t.getChatID() + "','"
					+ t.getFrom() + "','" + t.getDate() + "','" + t.getType() + "')");

		} catch (Exception e) {
			LOGGER.warn("saveTelegramToDB " + e);
		}
	}

	/**
	 * @return log
	 */
	public String getTelegramLog(String sFilter) {
		try {
			StringBuilder slog = new StringBuilder();
			int n = 0;
			Statement stmt = MsqlLite.getDB();
			// ResultSet rs = stmt.executeQuery("SELECT * FROM telegrambot WHERE msgid = '"
			// + nID + "';");
			ResultSet rs = stmt.executeQuery("SELECT * FROM telegramlog WHERE msgid IS NOT '0' ORDER BY msgdate ASC"); // DESC

			while (rs.next()) {
				n++;
				Date date = new Date(rs.getLong("msgdate") * 1000);
				StringBuilder sb = new StringBuilder();

				if (sFilter == null || !rs.getString("msgfrom").contains(sFilter)) {
					sb.append(n + ":\n");
					sb.append("msgid:  \t" + rs.getString("msgid") + "\n");
					sb.append("msg in: \t" + rs.getString("msg") + "\n");
					sb.append("msg out:\t" + rs.getString("antw") + "\n");
					sb.append("chatid: \t" + rs.getString("chatid") + "\n");
					sb.append("msgfrom:\t" + rs.getString("msgfrom") + "\n");
					sb.append("msgdate:\t" + date + "\n");
					sb.append("type:   \t" + rs.getString("type") + "\n");
					sb.append("----------------\n\n");
					slog.insert(0, sb);
				}
			}
			rs.close();
			return slog.toString();
		} catch (Exception e) {
			LOGGER.warn("getTelegram" + e);
			return "FAIL";
		}
	}

	/////////////////////////////

	/**
	 * @param t
	 * @return
	 */
	public String menueMsg(Telegram t) {
		// Admin Befehle
		if (isAdminUser(t)) {
			switch (t.getMessageStringPart(0)) {
			case "admin":
				// @formatter:off
				return 
					"Admin Befehle:" + "\n\n" + 
					"AddAMsgToDB" + "\n" + 
					"GetAllMsg" + "\n" +
					"GetAllMotd" + "\n" +
					"AddAMotd" + "\n" +
					"SendMotd" + "\n" + 
					"SendInfo" + "\n" +
					"GetMotd" + "\n" + 
					"DelMotd ID" +  "\n" +
					"DelMsg ID"+  "\n" +
					"DoPing IP" + "\n" +
					"DoNmap" + "\n" +
					"getIp" + "\n" +
					"SendIp" + "\n" +
					"send ID msg" + "\n" +
					"Essen (Hilfe für essen)";
				// @formatter:on

			// Anbindung an msg datenbank
			case "addamsgtodb":
				return addMsg(t);
			case "getallmsg":
				return getAllMsg();
			case "delmsg":
				return delMsg(t);

			// Anbindung an motd datenbank
			case "addamotd":
				return addMotd(t);
			case "getallmotd":
				return getAllMotd();
			case "delmotd":
				return delMotd(t);
			case "getmotd":
				return getMotd();

			// Auto Msg Tests
			case "sendmotd":
				sendDaylyMotd();
				return "OK";
			case "sendinfo":
				sendDaylyInfo();
				return "OK";

			case "doping":
				return wipf.ping(t.getMessageStringRawPart(1)).toString();
			case "shell":
				return wipf.shell(t.getMessageStringFirst());

			case "send":
				return sendToId(t);

			case "getip":
				return wipf.escapeString(wipf.getExternalIp());
			case "sendip": {
				sendExtIp();
				return "OK";
			}

			default:
				break;
			}
		}
		// Alle festen Antworten
		switch (t.getMessageStringPart(0)) {
		case "start":
			return "Wipfbot Version:" + QMain.VERSION + "\nInfos per 'info'";
		case "v":
		case "ver":
		case "version":
		case "info":
		case "about":
		case "hlp":
		case "hilfe":
		case "help":
		case "wipfbot":
			return "Wipfbot\nVersion " + QMain.VERSION + "\nCreated by Tobias Fritsch\nwipf2@web.de";
		case "r":
		case "rnd":
		case "zufall":
			return MOthers.zufall(t.getMessageStringPart(1), t.getMessageStringPart(2));
		case "c":
		case "cr":
		case "en":
		case "encrypt":
			// TODO
			return wipf.encrypt(t.getMessageStringFirst(), "TODO_KEY");
		case "d":
		case "de":
		case "dc":
		case "decrypt":
			// TODO
			return wipf.decrypt(t.getMessageStringFirst(), "TODO_KEY");
		case "t":
		case "ttt":
		case "tictactoe":
		case "play":
		case "game":
			return MTicTacToe.input(t);
		case "time":
		case "date":
		case "datum":
		case "uhr":
		case "zeit":
		case "clock":
		case "z":
			return MWipf.dateTime();
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return MOthers.getWitz();
		case "countmsg":
			return countMsg();
		case "countsend":
			return contSend();
		case "telestats":
			return MWipf.dateTime() + "\n" + countMsg() + "\n" + contSend();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: "
					+ t.getMid();
		case "essen":
		case "e":
			return MEssen.menueEssen(t);
		case "to":
		case "todo":
			return MTodoList.menueTodoList(t);
		default:
			// Alle db aktionen
			t = getMsg(t, 0);
			// ob keine Antwort in db gefunden
			if (t.getAntwort() != null) {
				return t.getAntwort();
			} else {
				switch (wipf.getRandomInt(4)) {
				case 0:
					return "Keine Antwort vorhanden";
				case 1:
					return "Leider ist keine Antwort vorhanden";
				case 2:
					return "Hierzu gibt es keine Antwort";
				default:
					return "Antwort nicht vorhanden";
				}
			}
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String delMsg(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM telemsg WHERE id = " + t.getMessageIntPart(1));
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemsg" + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String delMotd(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM telemotd WHERE id = " + t.getMessageIntPart(1));
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 */
	private String addMsg(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemsg (request, response, options, editby, date) VALUES " + "('"
					+ t.getMessageStringPart(1) + "','" + t.getMessageStringSecond() + "','" + null + "','"
					+ t.getFrom() + "','" + t.getDate() + "')");
			return "OK: " + t.getMessageStringPart(1);
		} catch (Exception e) {
			LOGGER.warn("add telemsg " + e);
			return "Fehler";
		}

	}

	/**
	 * @param t
	 */
	private String addMotd(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemotd (text, editby, date) VALUES " + "('"
					+ t.getMessageStringFirst() + "','" + t.getFrom() + "','" + t.getDate() + "')");
			return "IN";
		} catch (Exception e) {
			LOGGER.warn("add telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 */
	private String countMsg() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemsg;");
			return rs.getString("COUNT(*)") + " Antworten in der DB";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	private String countMotd() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemotd;");
			return rs.getString("COUNT(*)") + " Motds in der DB";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	private String getMotd() {
		try {
			String s = null;

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd ORDER BY RANDOM() LIMIT 1");
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				s = (rs.getString("text"));
			}
			rs.close();
			// return "Nachricht des Tages\n " + MTime.date() + "\n\n" + s;
			return s;

		} catch (Exception e) {
			LOGGER.warn("get telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @param nStelle
	 * @return
	 */
	private Telegram getMsg(Telegram t, Integer nStelle) {
		try {
			Map<String, String> mapS = new HashMap<>();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("select * from telemsg where request = '" + t.getMessageStringPart(nStelle) + "';");
			while (rs.next()) {
				mapS.put(rs.getString("response"), rs.getString("options"));
			}
			rs.close();
			if (mapS.size() != 0) {
				int nZufallsElement = wipf.getRandomInt(mapS.size());
				int n = 0;
				for (Map.Entry<String, String> entry : mapS.entrySet()) {
					if (n == nZufallsElement) {
						t.setAntwort(entry.getKey());
						t.setOptions(entry.getValue());
					}
					n++;
				}
			}

		} catch (Exception e) {
			LOGGER.warn("get telemsg " + e);
		}
		return t;
	}

	/**
	 * @param t
	 * @return
	 */
	private String getAllMotd() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("text") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 */
	private String getAllMsg() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemsg;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("request") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 */
	private String sendToId(Telegram t) {
		Telegram tSend = new Telegram();
		tSend.setChatID(t.getMessageIntPart(1));
		tSend.setAntwort(t.getMessageStringSecond());
		tSend.setType("from: " + t.getChatID());

		saveTelegramToDB(tSend);
		sendToTelegram(tSend);
		return "done";
	}

	/**
	 * 
	 */
	public void sendDaylyInfo() {
		Telegram t = new Telegram();
		t.setAntwort(MWipf.dateTimeMs() + "\n" + countMsg() + "\n" + countMotd() + "\n" + contSend() + "\n\nVersion:"
				+ QMain.VERSION);
		t.setChatID(798200105);

		saveTelegramToDB(t);
		sendToTelegram(t);
	}

	/**
	 * 
	 */
	public void sendDaylyMotd() {
		Telegram t = new Telegram();
		t.setAntwort(getMotd());
		t.setChatID(-387871959);

		saveTelegramToDB(t);
		sendToTelegram(t);
	}

	/**
	 * @param sMsg
	 */
	public Boolean sendMsgToGroup(String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(-387871959);

		saveTelegramToDB(t);
		sendToTelegram(t);
		return true;
	}

	/**
	 * 
	 */
	public void sendExtIp() {
		Telegram t = new Telegram();
		try {
			t.setAntwort("Neue IP: " + wipf.getExternalIp());
		} catch (IOException e) {
			t.setAntwort("Fehler t23");
		}
		t.setChatID(798200105);

		saveTelegramToDB(t);
		sendToTelegram(t);
	}

	/**
	 * TODO ids zu db
	 * 
	 * @param t
	 * @return
	 */
	private Boolean isAdminUser(Telegram t) {
		return (t.getChatID() == 798200105 || t.getChatID() == 522467648);
	}

}
