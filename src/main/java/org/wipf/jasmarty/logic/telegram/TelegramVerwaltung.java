package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.telegram.extensions.TAppOthers;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramVerwaltung {

	@Inject
	Wipf wipf;
	@Inject
	TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;

	private static final Logger LOGGER = Logger.getLogger("Telegram V");

	private Integer nFailCount;
	private Integer nOffsetID;
	private String sBotKey;

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telegramlog (msgid INTEGER, msg TEXT, antw TEXT, chatid INTEGER, msgfrom TEXT, msgdate INTEGER, type TEXT);");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemsg (id integer primary key autoincrement, request TEXT, response TEXT, options TEXT, editby TEXT, date INTEGER);");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemotd (id integer primary key autoincrement, text TEXT, editby TEXT, date INTEGER);");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param n
	 */
	public void setFailCount(Integer n) {
		this.nFailCount = n;
	}

	/**
	 * @param n
	 */
	public Integer getFailCount() {
		return this.nFailCount;
	}

	/**
	 * 
	 */
	public boolean loadConfig() {
		// Auf 0 setzen -> definierter zustand
		this.nOffsetID = 0;
		// Load bot config
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT val FROM config WHERE key = 'telegrambot';");
			this.sBotKey = (rs.getString("val"));
			stmt.close();
			return true;
		} catch (Exception e) {
			LOGGER.warn("telegrambot nicht in db gefunden."
					+ " Setzen mit 'curl -X POST localhost:8080/telegram/setbot/bot2343242:ABCDEF348590247354352343345'");
			return false;
		}
	}

	/**
	 * @param sBot
	 * @return
	 */
	public Boolean setbot(String sBot) {
		try {
			this.sBotKey = sBot;
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('telegrambot','" + this.sBotKey + "')");
			stmt.close();
			LOGGER.info("Bot Key: " + this.sBotKey);
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
			String sAntwort = wipf.escapeString(t.getAntwort());
			if (sAntwort == null || sAntwort.equals("")) {
				sAntwort = "Leere%20Antwort";
			}

			String sResJson = wipf.httpRequestPOST("https://api.telegram.org/" + this.sBotKey + "/sendMessage?chat_id="
					+ t.getChatID() + "&text=" + sAntwort);

			JSONObject jo = new JSONObject(sResJson);

			if (!jo.getBoolean("ok")) {
				LOGGER.warn("API fail:" + sResJson + " Antworttext: '" + sAntwort + "'");
			}

		} catch (Exception e) {
			LOGGER.warn("Telegram senden " + e);
		}
	}

	/**
	 * 
	 */
	@Metered
	public void readUpdateFromTelegram() {
		try {
			String sJson = "";
			if (this.nOffsetID == 0) {
				sJson = wipf.httpRequestPOST("https://api.telegram.org/" + this.sBotKey + "/getUpdates");
			} else {
				sJson = wipf.httpRequestPOST(
						"https://api.telegram.org/" + this.sBotKey + "/getUpdates?offset=" + this.nOffsetID);
			}

			JSONObject jo = new JSONObject(sJson);
			if (!jo.getBoolean("ok")) {
				LOGGER.warn("API fail:" + sJson);
				this.nFailCount++;
				return;
			}

			JSONArray ja = jo.getJSONArray("result");

			for (int nMsg = 0; nMsg < ja.length(); nMsg++) {
				if (nMsg >= 5) {
					// Nur 5 Nachrichten in einen Zug verarbeiten
					continue;
				}

				Telegram t = new Telegram();
				JSONObject joMsgFull = ja.getJSONObject(nMsg);
				this.nOffsetID = joMsgFull.getInt("update_id") + 1;
				JSONObject joMsg = joMsgFull.getJSONObject("message");

				try {
					// Nachricht einlesen -> gelesen -> löschen am Telegram server per id bei
					// nächster abfrage
					t.setMid(joMsg.getInt("message_id"));
					t.setMessage(wipf.escapeString(joMsg.getString("text").trim().replaceAll("\"", "\'")));
					t.setChatID(joMsg.getJSONObject("chat").getInt("id"));
					t.setType(joMsg.getJSONObject("chat").getString("type"));
					t.setDate(joMsg.getInt("date"));
					t.setFrom(joMsg.get("from").toString());

				} catch (Exception e) {
					// weiter da sticker oder ähnliches
					continue;
				}

				t.setAntwort(menueMsg(t));
				saveTelegramToDB(t);
				sendToTelegram(t);

			}

			if (this.nFailCount != 0) {
				// Wenn Telegram wieder erreichbar ist info senden:
				sendExtIp();
			}

			// Da Telegram erreichbar ist:
			this.nFailCount = 0;

		} catch (Exception e) {
			this.nFailCount++;
			LOGGER.warn("readUpdateFromTelegram fails: " + this.nFailCount + " " + e);
		}

	}

	/**
	 * @param t
	 */
	public void saveTelegramToDB(Telegram t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT INTO telegramlog (msgid, msg, antw, chatid, msgfrom, msgdate, type)" + " VALUES ('"
					+ t.getMid() + "','" + t.getMessage() + "','" + t.getAntwort() + "','" + t.getChatID() + "','"
					+ t.getFrom() + "','" + t.getDate() + "','" + t.getType() + "')");
			stmt.close();
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
			Statement stmt = SqlLite.getDB();
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
			stmt.close();
			return slog.toString();
		} catch (Exception e) {
			LOGGER.warn("getTelegram" + e);
			return "FAIL";
		}
	}

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
			return "Wipfbot Version:" + MainHome.VERSION + "\nInfos per 'info'";
		case "v":
		case "ver":
		case "version":
		case "info":
		case "about":
		case "hlp":
		case "hilfe":
		case "help":
		case "wipfbot":
			return "Wipfbot\nVersion " + MainHome.VERSION + "\nCreated by Tobias Fritsch\nwipf2@web.de";
		case "r":
		case "rnd":
		case "zufall":
			return appOthers.zufall(t.getMessageStringPart(1), t.getMessageStringPart(2));
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
			return appTicTacToe.input(t);
		case "time":
		case "date":
		case "datum":
		case "uhr":
		case "zeit":
		case "clock":
		case "z":
			return wipf.time("dd.MM.yyyy HH:mm:ss");
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return appOthers.getWitz();
		case "countmsg":
			return countMsg();
		case "countsend":
			return contSend();
		case "telestats":
			return wipf.time("dd.MM.yyyy HH:mm:ss") + "\n" + countMsg() + "\n" + contSend();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: "
					+ t.getMid();
		case "to":
		case "todo":
			return appTodoList.telegramMenueTodoList(t);
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM telemsg WHERE id = " + t.getMessageIntPart(1));
			stmt.close();
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM telemotd WHERE id = " + t.getMessageIntPart(1));
			stmt.close();
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemsg (request, response, options, editby, date) VALUES " + "('"
					+ t.getMessageStringPart(1) + "','" + t.getMessageStringSecond() + "','" + null + "','"
					+ t.getFrom() + "','" + t.getDate() + "')");
			stmt.close();
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemotd (text, editby, date) VALUES " + "('"
					+ t.getMessageStringFirst() + "','" + t.getFrom() + "','" + t.getDate() + "')");
			stmt.close();
			return "IN";
		} catch (Exception e) {
			LOGGER.warn("add telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 * 
	 *         TODO del this
	 */
	private String countMsg() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemsg;");
			String s = rs.getString("COUNT(*)") + " Antworten in der DB";
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler countMsg";
		}
	}

	/**
	 * @return
	 * 
	 *         TODO del this
	 */
	private String countMotd() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemotd;");
			String s = rs.getString("COUNT(*)") + " Motds in der DB";
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler countMotd";
		}
	}

	/**
	 * @return
	 */
	private String contSend() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telegramlog;");
			stmt.close();
			return rs.getString("COUNT(*)") + " Nachrichten gesendet";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler contSend";
		}
	}

	/**
	 * @return
	 */
	private String getMotd() {
		try {
			String s = null;

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd ORDER BY RANDOM() LIMIT 1");
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				s = (rs.getString("text"));
			}
			stmt.close();
			return s;

		} catch (Exception e) {
			LOGGER.warn("get telemotd " + e);
			return "Fehler motd";
		}
	}

	/**
	 * @param t
	 * @param nStelle
	 * @return
	 * 
	 *         TODO del this
	 */
	private Telegram getMsg(Telegram t, Integer nStelle) {
		try {
			Map<String, String> mapS = new HashMap<>();

			Statement stmt = SqlLite.getDB();
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
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("get telemsg " + e);
		}
		return t;
	}

	/**
	 * @param t
	 * @return
	 * 
	 *         TODO del this -> json
	 */
	private String getAllMotd() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("text") + "\n");
			}
			stmt.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 * 
	 *         TODO del this -> json
	 */
	private String getAllMsg() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemsg;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("request") + "\n");
			}
			stmt.close();
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
		t.setAntwort(wipf.time("dd.MM.yyyy HH:mm:ss;SSS") + "\n" + countMsg() + "\n" + countMotd() + "\n" + contSend()
				+ "\n\nVersion:" + MainHome.VERSION);
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
		t.setAntwort("Neue IP: " + wipf.getExternalIp());
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
