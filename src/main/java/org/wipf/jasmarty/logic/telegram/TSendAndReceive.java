package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.FileVW;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.MultipartUtility;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.listen.RndEventsService;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TSendAndReceive {

	@Inject
	Wipf wipf;
	@Inject
	TeleLogService tLog;
	@Inject
	TAppMsgService appMsg;
	@Inject
	RndEventsService appRndEvent;
	@Inject
	TeleMenue menue;
	@Inject
	TUserAndGroups userAndGroups;
	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	FileVW fileVw;
	@Inject
	TUsercache tUsercache;

	private static final Logger LOGGER = Logger.getLogger("Telegram SendAndReceive");

	// Auf 0 setzen -> definierter zustand zum Start
	private Integer nOffsetID = 0;
	private String sBotKey;

	/**
	 * 
	 */
	@PostConstruct
	public void loadConfig() {
		this.sBotKey = wipfConfig.getConfParamString("telegrambot");
	}

	/**
	 * @param sBot
	 * @return
	 * 
	 */
	public Boolean setbot(String sBot) throws WipfException {
		this.sBotKey = sBot;
		wipfConfig.setConfParam("telegrambot", sBot);
		LOGGER.info("Bot Key: " + getBotKeyFromCache());
		return true;
	}

	/**
	 * @return
	 * @throws WipfException
	 */
	public String getBotKeyFromCache() throws WipfException {
		if (wipf.isValid(sBotKey) && !this.sBotKey.equals("null") && !this.sBotKey.equals("undefined"))
			return this.sBotKey;
		throw new WipfException("Kein Botkey gesetzt!");
	}

	/**
	 * 
	 */
	@Metered
	public char readUpdateFromTelegram() {
		try {
			JSONObject jo = new JSONObject(wipf.httpRequest(Wipf.httpRequestType.POST, "https://api.telegram.org/" + getBotKeyFromCache() + "/getUpdates?offset=" + this.nOffsetID));

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
					t.setChatID(joMsg.getJSONObject("chat").getLong("id"));
					t.setType(joMsg.getJSONObject("chat").getString("type"));
					t.setDate(joMsg.getInt("date"));
					t.setFrom(joMsg.get("from").toString());

					if (joMsg.has("text")) {
						// Normale Textnachricht
						t.setMessage(wipf.escapeStringSaveCode(joMsg.getString("text")));
						String antwort = menue.menueMsg(t);
						if (antwort == null) {
							break;
						}
						t.setAntwort(antwort);
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
	private String saveVoice(JSONObject joMsg) throws Exception {
		JSONObject jd = new JSONObject(joMsg.get("voice").toString());

		String sFileId = jd.get("file_id").toString();
		String sFileName = "voice_" + joMsg.get("message_id").toString() + ".oga";

		String sFileInfoPath = "https://api.telegram.org/" + getBotKeyFromCache() + "/getFile?file_id=" + sFileId;
		return teleFileDownload(sFileInfoPath, sFileName);
	}

	/**
	 * @param joMsg
	 * @return
	 */
	private String saveAudio(JSONObject joMsg) throws Exception {
		JSONObject jd = new JSONObject(joMsg.get("document").toString());

		String sFileId = jd.get("file_id").toString();
		String sFileName = "audio_" + joMsg.get("message_id").toString() + "_" + jd.get("file_name").toString();

		String sFileInfoPath = "https://api.telegram.org/" + getBotKeyFromCache() + "/getFile?file_id=" + sFileId;
		return teleFileDownload(sFileInfoPath, sFileName);
	}

	/**
	 * @param joMsg
	 * @return
	 */
	private String saveDocument(JSONObject joMsg) throws Exception {
		// nur bestimme Ids zulassen
		Long nChatId = joMsg.getJSONObject("chat").getLong("id");
		if (userAndGroups.isUser(nChatId)) {
			JSONObject jd = new JSONObject(joMsg.get("document").toString());

			String sFileId = jd.get("file_id").toString();
			String sFileName = "doc_" + joMsg.get("message_id").toString() + "_" + jd.get("file_name").toString();

			String sFileInfoPath = "https://api.telegram.org/" + getBotKeyFromCache() + "/getFile?file_id=" + sFileId;
			return teleFileDownload(sFileInfoPath, sFileName);
		} else {
			return "Nich erlaubt";
		}
	}

	/**
	 * @param joMsg
	 */
	private String saveBestPhoto(JSONObject joMsg) throws Exception {
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

		String sFileInfoPath = "https://api.telegram.org/" + getBotKeyFromCache() + "/getFile?file_id=" + oBiggestSize.get("file_id").toString();
		String sFilename = "pic_" + joMsg.get("message_id").toString() + ".jpg";

		return teleFileDownload(sFileInfoPath, sFilename);
	}

	/**
	 * @param sFileInfoPath
	 * @param sFilename
	 * @return
	 * @throws Exception
	 */
	private String teleFileDownload(String sFileInfoPath, String sFilename) throws Exception {
		try {
			JSONObject joDownload = new JSONObject(wipf.httpRequest(Wipf.httpRequestType.GET, sFileInfoPath));
			JSONObject picNam1 = new JSONObject(joDownload.get("result").toString());
			String sPicPath = picNam1.get("file_path").toString();
			String sPicUrl = "https://api.telegram.org/file/" + getBotKeyFromCache() + "/" + sPicPath;

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
			wipf.sleep(2000);
		}

	}

	/**
	 * nur public das @Metered funktioniert
	 * 
	 * @param t
	 */
	@Metered
	public void sendToTelegram(Telegram t) {
		tLog.saveTelegramToLog(t);
		String sAntwort = t.getAntwort();
		if (sAntwort == null || sAntwort.equals("")) {
			sAntwort = "Leere Antwort";
		}

		try {
			String sResJson = wipf.httpRequest(Wipf.httpRequestType.POST, "https://api.telegram.org/" + getBotKeyFromCache() + "/sendMessage?chat_id=" + t.getChatID() + "&text=" + wipf.encodeUrlString(sAntwort));
			JSONObject jo = new JSONObject(sResJson);

			if (!jo.getBoolean("ok")) {
				LOGGER.warn("API fail:" + sResJson + " Antworttext: '" + sAntwort + "'");
			}

		} catch (Exception e) {
			LOGGER.error("Telegram senden " + e + " - " + t.toJson().toString());
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public String sendDocumentToTelegram(Telegram t) {
		try {
			return sendDocumentToTelegram(t.getChatID(), t.getMessageFullWithoutFirstWord());
		} catch (Exception e) {
			return "Fehler " + e;
		}
	}

	/**
	 * @param nChatId
	 * @param sFilePath
	 * @return
	 * @throws IOException
	 */
	public String sendDocumentToTelegram(Long nChatId, String sFilePath) {
		return sendFileToTelegram(nChatId, fileVw.getFile(sFilePath));
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String sendDatabaseToTelegram(Telegram t) {
		try {
			return sendFileToTelegram(t.getChatID(), fileVw.getDataBaseAsFile());
		} catch (Exception e) {
			return "Fehler " + e;
		}
	}

	/**
	 * 
	 */
	public void sendDaylyInfo() {
		Telegram t = new Telegram();
		t.setAntwort(wipf.getTime("dd.MM.yyyy HH:mm:ss;SSS") + "\n" + appMsg.countMsg() + "\n" + tLog.countMsg().toString() + "\n\nVersion:" + MainHome.VERSION + "\n\n" + "User: " + tUsercache.getAnzahl());
		t.setChatID(userAndGroups.getAdminId());

		sendTelegram(t);
	}

	/**
	 * 
	 */
	public void sendRndEventToAdmin() {
		Telegram t = new Telegram();
		t.setAntwort("Vorschlag für jetzt:" + "\n" + appRndEvent.getRndEventRnd());
		t.setChatID(userAndGroups.getAdminId());
		sendTelegram(t);
	}

	/**
	 * @param sMsg
	 */
	public void sendMsgTo(Long nGroupId, String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(nGroupId);

		sendTelegram(t);
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
	 * @param nChatId
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private String sendFileToTelegram(Long nChatId, File f) {
		LOGGER.info("upload File to " + nChatId + " / " + f.getPath());
		try {
			MultipartUtility multipart;
			multipart = new MultipartUtility("https://api.telegram.org/" + getBotKeyFromCache() + "/sendDocument?chat_id=" + nChatId, "UTF-8");
			// multipart.addFormField("param_name_1", "param_value");
			multipart.addFilePart("document", f);
			String response = multipart.finish();
			JSONObject jo = new JSONObject(response);
			return (jo.get("ok").toString());
		} catch (IOException | WipfException e) {
			e.printStackTrace();
			return "Fehler 116 " + e;
		}
	}

}
