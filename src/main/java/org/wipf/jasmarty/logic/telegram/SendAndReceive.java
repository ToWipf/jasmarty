package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.MultipartUtility;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SendAndReceive {

	@Inject
	Wipf wipf;
	@Inject
	TeleLog tLog;
	@Inject
	TAppMsg appMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppEssen appEssen;
	@Inject
	TeleMenue menue;
	@Inject
	UserAndGroups userAndGroups;
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("Telegram SendAndReceive");

	private Integer nOffsetID;
	private String sBotKey;

	/**
	 * @throws SQLException
	 * 
	 */
	public boolean loadConfig() throws SQLException {
		// Auf 0 setzen -> definierter zustand zum Start
		this.nOffsetID = 0;
		// Load bot config
		this.sBotKey = wipfConfig.getConfParamString("telegrambot");

		if (this.sBotKey == null || this.sBotKey.equals("null") || this.sBotKey.equals("")) {
			LOGGER.warn("telegrambot nicht in db gefunden."
					+ " Setzen mit 'curl -X POST localhost:8080/telegram/setbot/bot2343242:ABCDEF348590247354352343345'");
			return false;
		}
		return true;
	}

	/**
	 * @param sBot
	 * @return
	 * @throws SQLException
	 */
	public Boolean setbot(String sBot) throws SQLException {
		this.sBotKey = sBot;
		wipfConfig.setConfParam("telegrambot", sBot);
		LOGGER.info("Bot Key: " + this.sBotKey);
		return true;
	}

	/**
	 * 
	 */
	@Metered
	public char readUpdateFromTelegram() {

		StringBuilder sbHttpRequestTelegram = new StringBuilder();
		sbHttpRequestTelegram.append("https://api.telegram.org/");
		sbHttpRequestTelegram.append(this.sBotKey);
		sbHttpRequestTelegram.append("/getUpdates");

		if (this.nOffsetID != 0) {
			sbHttpRequestTelegram.append("?offset=");
			sbHttpRequestTelegram.append(this.nOffsetID);
		}

		try {
			JSONObject jo = new JSONObject(wipf.httpRequest(Wipf.httpRequestType.POST,
					"https://api.telegram.org/" + this.sBotKey + "/getUpdates?offset=" + this.nOffsetID));

			if (!jo.getBoolean("ok")) {
				LOGGER.warn("Telegram nicht 'ok'");
				return 'f';
			}

			JSONArray ja = jo.getJSONArray("result");

			if (ja.length() > 0) {

				for (int nMsg = 0; nMsg < ja.length(); nMsg++) {
					if (nMsg >= 5) {
						// Nur 5 Nachrichten in einen Zug verarbeiten, der rest wird später neu abgeholt
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

					} catch (JSONException e) {
						// Sticker oder ähnliches
						t.setMessage("fail");
					}

					t.setAntwort(menue.menueMsg(t));
					sendTelegram(t);
				}
				// Fertig mit neuen Nachrichten
				return 'n';
			} else {
				// Keine neue Nachrichten
				return 'o';
			}
		} catch (Exception e) {
			LOGGER.warn("readUpdateFromTelegram fails: " + e);
			return 'f';
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public void sendTelegram(Telegram t) {
		// Lange Nachrichten splitten (max 4096 chars)
		String sAntwortToSend = t.getAntwort();
		for (String sPart : sAntwortToSend.split("(?<=\\G.{3900})")) {
			t.setAntwort(sPart);
			sendToTelegram(t);
			// Um das Maximale Sendelimit nicht zu erreichen
			wipf.sleep(4000);
		}
	}

	/**
	 * nur public das @Metered funktioniert
	 * 
	 * @param t
	 */
	@Metered
	public void sendToTelegram(Telegram t) {
		tLog.saveToLog(t);
		String sAntwort = t.getAntwort();
		if (sAntwort == null || sAntwort.equals("")) {
			sAntwort = "Leere Antwort";
		}

		try {
			String sResJson = wipf.httpRequest(Wipf.httpRequestType.POST, "https://api.telegram.org/" + this.sBotKey
					+ "/sendMessage?chat_id=" + t.getChatID() + "&text=" + wipf.encodeUrlString(sAntwort));

			JSONObject jo = new JSONObject(sResJson);

			if (!jo.getBoolean("ok")) {
				LOGGER.warn("API fail:" + sResJson + " Antworttext: '" + sAntwort + "'");
			}

		} catch (Exception e) {
			LOGGER.warn("Telegram senden " + e);
		}
	}

	/**
	 * @param sChatID
	 * @param sFilePath
	 * @return
	 * @throws IOException
	 */
	public String sendPictureToTelegram(Integer nChatId, String sFilePath) throws IOException {
		MultipartUtility multipart = new MultipartUtility(
				"https://api.telegram.org/" + this.sBotKey + "/sendPhoto?chat_id=" + nChatId, "UTF-8");
		// multipart.addFormField("param_name_1", "param_value");
		multipart.addFilePart("photo", new File(sFilePath));
		String response = multipart.finish();
		return (response);
	}

	/**
	 * 
	 */
	public void sendDaylyInfo() {
		Telegram t = new Telegram();
		t.setAntwort(wipf.getTime("dd.MM.yyyy HH:mm:ss;SSS") + "\n" + appMsg.countMsg() + "\n" + appMotd.countMotd()
				+ "\n" + tLog.countMsg() + "\n\nVersion:" + MainHome.VERSION);
		t.setChatID(userAndGroups.getAdminId());

		sendTelegram(t);
	}

	/**
	 * @param sMsg
	 */
	public void sendMsgTo(Integer nGroupId, String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(nGroupId);

		sendTelegram(t);
	}

	/**
	 * 
	 */
	public void sendDaylyMotd(Integer nGroupId) {
		Telegram t = new Telegram();
		t.setAntwort(appMotd.getRndMotd());
		t.setChatID(nGroupId);

		sendTelegram(t);
	}

	/**
	 * 
	 */
	public void sendExtIp() {
		sendMsgToAdmin("Neue IP: " + wipf.getExternalIp());
	}

	/**
	 * 
	 */
	public void sendMsgToAdmin(String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(userAndGroups.getAdminId());
		sendTelegram(t);
	}

	/**
	 * @throws SQLException
	 * 
	 */
	// TODO einbinden
	public void sendDaylyEssen(Integer nGroupId) throws SQLException {
		Telegram t = new Telegram();
		t.setAntwort("Vorschlag für heute:" + "\n" + appEssen.getEssenRnd());
		t.setChatID(nGroupId);

		sendTelegram(t);
	}

	/**
	 * @return
	 */
	public String getBotKey() {
		return this.sBotKey;
	}

}
