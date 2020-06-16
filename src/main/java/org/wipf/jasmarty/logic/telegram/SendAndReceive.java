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
import org.wipf.jasmarty.logic.telegram.messageEdit.MsgLog;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppMotd;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppTeleMsg;
import org.wipf.jasmarty.logic.telegram.messageEdit.TeleMenue;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SendAndReceive {

	@Inject
	Wipf wipf;
	@Inject
	MsgLog msglog;
	@Inject
	TAppTeleMsg appTeleMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TeleMenue menue;

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
			String sAntwort = t.getAntwort();
			if (sAntwort == null || sAntwort.equals("")) {
				sAntwort = "Leere Antwort";
			}

			String sResJson = wipf.httpRequestPOST("https://api.telegram.org/" + this.sBotKey + "/sendMessage?chat_id="
					+ t.getChatID() + "&text=" + wipf.encodeUrlString(sAntwort));

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

				t.setAntwort(menue.menueMsg(t));
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

}
