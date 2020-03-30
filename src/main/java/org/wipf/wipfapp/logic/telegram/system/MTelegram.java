package org.wipf.wipfapp.logic.telegram.system;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.Telegram;
import org.wipf.wipfapp.logic.base.MsqlLite;
import org.wipf.wipfapp.logic.base.Wipfapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;

/**
 * @author wipf
 *
 */
public class MTelegram {

	private static final Logger LOGGER = Logger.getLogger("MTelegram");

	/**
	 * 
	 */
	public static void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telegramlog (msgid INTEGER, msg TEXT, antw TEXT, chatid INTEGER, msgfrom TEXT, msgdate INTEGER, type TEXT);");

		} catch (Exception e) {
			LOGGER.warn("initDB Telegram " + e);
		}
	}

	/**
	 * 
	 */
	public static boolean loadConfig() {
		// Auf 0 setzen -> definierter zustand
		Wipfapp.TelegramOffsetID = 0;
		// Load bot config
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT val FROM settings WHERE id = 'telegrambot';");
			Wipfapp.BOTKEY = (rs.getString("val"));
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
	public static Boolean setbot(String sBot) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM settings WHERE id = 'telegrambot'");
			stmt.execute("INSERT INTO settings (id, val) VALUES ('telegrambot','" + sBot + "')");
			Wipfapp.BOTKEY = sBot;
			LOGGER.info("Bot Key: " + Wipfapp.BOTKEY);

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
	public static void sendToTelegram(Telegram t) {
		try {
			String sAntwort = t.getAntwort();
			if (sAntwort == null || sAntwort.equals("")) {
				sAntwort = "Leere%20Antwort";
			}

			String sResJson = Unirest.post("https://api.telegram.org/" + Wipfapp.BOTKEY + "/sendMessage?chat_id="
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
	public static void readUpdateFromTelegram() {
		try {
			String sJson;
			if (Wipfapp.TelegramOffsetID == 0) {
				sJson = Unirest.post("https://api.telegram.org/" + Wipfapp.BOTKEY + "/getUpdates").asString().getBody();
			} else {
				sJson = Unirest.post(
						"https://api.telegram.org/" + Wipfapp.BOTKEY + "/getUpdates?offset=" + Wipfapp.TelegramOffsetID)
						.asString().getBody();
			}
			// parse josn
			ObjectMapper mapper = new ObjectMapper();
			ArrayList<Telegram> li = new ArrayList<>();

			JsonNode jn = mapper.readTree(sJson);

			if (!jn.get("ok").asBoolean()) {
				LOGGER.warn("API fail:" + sJson);
				Wipfapp.FailCountTelegram++;
				return;
			}

			for (JsonNode n : jn) {
				for (JsonNode nn : n) {
					Telegram t = new Telegram();
					try {
						// Nachricht gelesen -> löschen am Telegram server
						Wipfapp.TelegramOffsetID = nn.get("update_id").asInt() + 1;
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
				Wipfapp.TelegramOffsetID = Wipfapp.TelegramOffsetID - li.size() + 5;
			}

			// ids zu db
			Integer nMax = 0;
			for (Telegram t : li) {
				nMax++;
				if (nMax <= 5) {
					try {
						t.setAntwort(MTeleMsg.menueMsg(t));
						saveTelegramToDB(t);
						sendToTelegram(t);
					} catch (Exception e) {
						LOGGER.warn("bearbeiteMsg " + e);
					}
				}
			}
			if (Wipfapp.FailCountTelegram != 0) {
				MTeleMsg.sendExtIp();
			}
			Wipfapp.FailCountTelegram = 0;

		} catch (

		Exception e) {
			Wipfapp.FailCountTelegram++;
			LOGGER.warn("readUpdateFromTelegram fails: " + Wipfapp.FailCountTelegram + " " + e);
		}
	}

	/**
	 * @return
	 */
	public static String contSend() {
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
	public static void saveTelegramToDB(Telegram t) {
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
	public static String getTelegramLog(String sFilter) {
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

	/**
	 * TODO ids zu db
	 * 
	 * @param t
	 * @return
	 */
	public static Boolean isAdminUser(Telegram t) {
		return (t.getChatID() == 798200105 || t.getChatID() == 522467648);
	}
}
