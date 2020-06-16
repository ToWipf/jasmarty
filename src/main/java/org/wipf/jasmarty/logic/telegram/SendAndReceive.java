package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.Statement;

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
import org.wipf.jasmarty.logic.telegram.extensions.TAppMotd;
import org.wipf.jasmarty.logic.telegram.extensions.TAppOthers;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTeleMsg;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SendAndReceive {

	@Inject
	Wipf wipf;
	@Inject
	TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	MsgLog msglog;
	@Inject
	AdminUser adminUser;
	@Inject
	TAppTeleMsg appTeleMsg;
	@Inject
	TAppMotd appMotd;

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
	@Metered
	private void sendToTelegram(Telegram t) {
		try {
			String sAntwort = wipf.escapeStringHtml(t.getAntwort());
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
				// Nachricht einlesen -> gelesen -> löschen am Telegram server per update_id
				this.nOffsetID = joMsgFull.getInt("update_id") + 1;
				JSONObject joMsg = joMsgFull.getJSONObject("message");

				t.setMid(joMsg.getInt("message_id"));
				t.setChatID(joMsg.getJSONObject("chat").getInt("id"));
				t.setType(joMsg.getJSONObject("chat").getString("type"));
				t.setDate(joMsg.getInt("date"));
				t.setFrom(joMsg.get("from").toString());

				try {
					// Normale Textnachricht
					t.setMessage(wipf.escapeStringSaveCode(joMsg.getString("text")));

				} catch (Exception e) {
					// Sticker oder ähnliches
					t.setMessage("fail");
				}

				t.setAntwort(menueMsg(t));
				msglog.saveTelegramToLog(t);
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
	 * @return
	 */
	public String menueMsg(Telegram t) {
		// Admin Befehle
		if (adminUser.isAdminUser(t)) {
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
				return appTeleMsg.addMsg(t);
			case "getallmsg":
				return appTeleMsg.getAllMsg();
			case "delmsg":
				return appTeleMsg.delMsg(t);

			// Anbindung an motd datenbank
			case "addamotd":
				return appMotd.addMotd(t);
			case "getallmotd":
				return appMotd.getAllMotd();
			case "delmotd":
				return appMotd.delMotd(t);
			case "getmotd":
				return appMotd.getRndMotd();

			case "doping":
				return wipf.ping(t.getMessageStringPart(1)).toString();
			case "shell":
				return wipf.shell(t.getMessageFullWithoutFirstWord());

			case "send":
				return sendToId(t);

			case "getip":
				return wipf.escapeStringHtml(wipf.getExternalIp());
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
			return wipf.encrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
		case "d":
		case "de":
		case "dc":
		case "decrypt":
			// TODO
			return wipf.decrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
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
			return appTeleMsg.countMsg();
		case "countsend":
			return msglog.contSend();
		case "telestats":
			return wipf.time("dd.MM.yyyy HH:mm:ss") + "\n" + appTeleMsg.countMsg() + "\n" + msglog.contSend();
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
			t = appTeleMsg.getMsg(t, 0);
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
	private String sendToId(Telegram t) {
		Telegram tSend = new Telegram();
		tSend.setChatID(t.getMessageIntPart(1));
		tSend.setAntwort(t.getMessageFullWithoutSecondWord());
		tSend.setType("from: " + t.getChatID());

		msglog.saveTelegramToLog(tSend);
		sendToTelegram(tSend);
		return "done";
	}

	/**
	 * 
	 */
	public void sendDaylyInfo() {
		Telegram t = new Telegram();
		t.setAntwort(wipf.time("dd.MM.yyyy HH:mm:ss;SSS") + "\n" + appTeleMsg.countMsg() + "\n" + appMotd.countMotd()
				+ "\n" + msglog.contSend() + "\n\nVersion:" + MainHome.VERSION);
		t.setChatID(798200105);

		msglog.saveTelegramToLog(t);
		sendToTelegram(t);
	}

	/**
	 * @param sMsg
	 */
	public Boolean sendMsgToGroup(String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(-387871959);

		msglog.saveTelegramToLog(t);
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

		msglog.saveTelegramToLog(t);
		sendToTelegram(t);
	}

	/**
	 * 
	 */
	public void sendDaylyMotd() {
		Telegram t = new Telegram();
		t.setAntwort(appMotd.getRndMotd());
		t.setChatID(-387871959);

		msglog.saveTelegramToLog(t);
		sendToTelegram(t);
	}

	/**
	 * @return
	 */
	public String getBotKey() {
		return this.sBotKey;
	}

}
