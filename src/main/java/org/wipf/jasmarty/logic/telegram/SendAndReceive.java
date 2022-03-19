package org.wipf.jasmarty.logic.telegram;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.FileVW;
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
	@Inject
	FileVW fileVw;

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

					if (joMsg.has("text")) {
						// Normale Textnachricht
						t.setMessage(wipf.escapeStringSaveCode(joMsg.getString("text")));
						t.setAntwort(menue.menueMsg(t));
					} else if (joMsg.has("photo")) {
						t.setAntwort(saveBestPhoto(joMsg));
					} else if (joMsg.has("document")) {
						t.setAntwort(saveDocument(joMsg));
					} else if (joMsg.has("voice")) {
						t.setAntwort(saveVoice(joMsg));
					} else if (joMsg.has("audio")) {
						t.setAntwort(saveAudio(joMsg));
					} else if (joMsg.has("location")) {
						t.setAntwort(joMsg.get("location").toString());
					} else if (joMsg.has("contact")) {
						t.setAntwort(joMsg.get("contact").toString());
					} else {
						// z.B. poll - Umfrage
						t.setAntwort("Dies konnte nicht bearbeitet werden");
					}

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
	 * @param joMsg
	 * @return
	 */
	private String saveVoice(JSONObject joMsg) {
		JSONObject jd = new JSONObject(joMsg.get("voice").toString());

		String sFileId = jd.get("file_id").toString();
		String sFileName = "voice_" + joMsg.get("message_id").toString() + ".oga";

		String sFileInfoPath = "https://api.telegram.org/" + this.sBotKey + "/getFile?file_id=" + sFileId;
		return teleFileDownload(sFileInfoPath, sFileName);
	}

	/**
	 * @param joMsg
	 * @return
	 */
	private String saveAudio(JSONObject joMsg) {
		JSONObject jd = new JSONObject(joMsg.get("document").toString());

		String sFileId = jd.get("file_id").toString();
		String sFileName = "audio_" + joMsg.get("message_id").toString() + "_" + jd.get("file_name").toString();

		String sFileInfoPath = "https://api.telegram.org/" + this.sBotKey + "/getFile?file_id=" + sFileId;
		return teleFileDownload(sFileInfoPath, sFileName);
	}

	/**
	 * @param joMsg
	 * @return
	 */
	private String saveDocument(JSONObject joMsg) {
		// nur bestimme Ids zulassen
		Integer nChatId = joMsg.getJSONObject("chat").getInt("id");
		if (userAndGroups.isUser(nChatId)) {
			JSONObject jd = new JSONObject(joMsg.get("document").toString());

			String sFileId = jd.get("file_id").toString();
			String sFileName = "doc_" + joMsg.get("message_id").toString() + "_" + jd.get("file_name").toString();

			String sFileInfoPath = "https://api.telegram.org/" + this.sBotKey + "/getFile?file_id=" + sFileId;
			return teleFileDownload(sFileInfoPath, sFileName);
		} else {
			return "Nich erlaubt";
		}
	}

	/**
	 * @param joMsg
	 */
	private String saveBestPhoto(JSONObject joMsg) {
		JSONArray jap = new JSONArray(joMsg.get("photo").toString());
		JSONObject oBiggestSize = null;
		Integer nBiggestSize = 0;

		for (Object j : jap) {
			// Besste Auflösung finden
			JSONObject o = new JSONObject(j.toString());
			Integer nFSize = Integer.valueOf(o.get("file_size").toString());
			if (nBiggestSize < nFSize) {
				nBiggestSize = nFSize;
				oBiggestSize = o;
			}
		}

		if (oBiggestSize == null) {
			return "Fehler bei Photo F1";
		}

		String sFileInfoPath = "https://api.telegram.org/" + this.sBotKey + "/getFile?file_id="
				+ oBiggestSize.get("file_id").toString();
		String sFilename = "pic_" + joMsg.get("message_id").toString() + ".jpg";

		return teleFileDownload(sFileInfoPath, sFilename);
	}

	/**
	 * @param sFileInfoPath
	 * @param sFilename
	 * @return
	 */
	private String teleFileDownload(String sFileInfoPath, String sFilename) {
		try {
			JSONObject joDownload = new JSONObject(wipf.httpRequest(Wipf.httpRequestType.GET, sFileInfoPath));
			JSONObject picNam1 = new JSONObject(joDownload.get("result").toString());
			String sPicPath = picNam1.get("file_path").toString();
			String sPicUrl = "https://api.telegram.org/file/" + this.sBotKey + "/" + sPicPath;

			if (fileVw.saveFileToDisk(sPicUrl, sFilename)) {
				return "speichern als " + sFilename;
			} else {
				return "Fehler bei teleFileDownload F1";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fehler bei teleFileDownload F2";
	}

	/**
	 * TODO Maximal 4000 Zeichen splitten (max 4096 chars) oder 240 Zeilen
	 * 
	 * Lange Nachrichten aufteilen
	 * 
	 * @return
	 */
	private List<String> splitStringToMsg(String sNachricht) {
		List<String> output = new LinkedList<>();
		String[] lines = sNachricht.split("\r\n|\r|\n");

		Integer nZeile = 0;
		StringBuilder sb = new StringBuilder();
		for (String sZeile : lines) {
			nZeile++;

			if (nZeile < 240) {
				// 239 Zeilen nehmen dann splitten
				if (sb.length() != 0) {
					sb.append("\n");
				}
				sb.append(sZeile);
			} else {
				// Zeile 240
				// Output wegspeichern
				output.add(sb.toString());
				// Cache leerern
				sb.delete(0, sb.length());
				// Die Zeile dieses Duchlaufs auch cachen
				sb.append(sZeile);
				// Neu zählen
				nZeile = 0;
			}
		}
		// Den Restlichen Cache bearbeiten
		output.add(sb.toString());
		return output;
	}

	/**
	 * @param t
	 * @return
	 */
	public void sendTelegram(Telegram t) {
		for (String sPart : splitStringToMsg(t.getAntwort())) {
			t.setAntwort(sPart);
			sendToTelegram(t);
			// Um das Maximale Sendelimit nicht zu erreichen, 4 sek. warten
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
	// TODO löschen
	// public String sendPictureToTelegram(Integer nChatId, String sFilePath) throws
	// IOException {
	// MultipartUtility multipart = new MultipartUtility(
	// "https://api.telegram.org/" + this.sBotKey + "/sendPhoto?chat_id=" + nChatId,
	// "UTF-8");
	// // multipart.addFormField("param_name_1", "param_value");
	// multipart.addFilePart("photo", new File("files/" + sFilePath));
	// String response = multipart.finish();
	// return (response);
	// }

	/**
	 * @param t
	 * @return
	 */
	public String sendDocumentToTelegram(Telegram t) {
		try {
			sendDocumentToTelegram(t.getChatID(), t.getMessageFullWithoutFirstWord());
			return "Ok";
		} catch (IOException e) {
			return "Fehler " + e;
		}
	}

	/**
	 * @param nChatId
	 * @param sFilePath
	 * @return
	 * @throws IOException
	 */
	public String sendDocumentToTelegram(Integer nChatId, String sFilePath) throws IOException {
		MultipartUtility multipart = new MultipartUtility(
				"https://api.telegram.org/" + this.sBotKey + "/sendDocument?chat_id=" + nChatId, "UTF-8");
		// multipart.addFormField("param_name_1", "param_value");
		multipart.addFilePart("document", fileVw.getFile(sFilePath));
		String response = multipart.finish();
		LOGGER.info("upload File to " + nChatId + " / " + sFilePath);
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
