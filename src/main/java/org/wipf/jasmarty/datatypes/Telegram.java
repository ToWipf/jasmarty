package org.wipf.jasmarty.datatypes;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf
 *
 */
public class Telegram {

	private static final Logger LOGGER = Logger.getLogger("Telegram");

	private Integer nMid;
	private String sMessage;
	private String sAntwort;
	private Integer nChatID;
	private Integer nDate;
	private String sType;
	private String sFrom;
	private String sOptions;

	/**
	 * 
	 */
	public Telegram() {
		this.nMid = 0;
		this.sMessage = "";
	}

	/**
	 * @param copy of t
	 */
	public Telegram(Telegram t) {
		this.nChatID = t.nChatID;
		this.sMessage = t.sMessage;
		this.sAntwort = t.sAntwort;
		this.nMid = t.nMid;
		this.nDate = t.nDate;
		this.sType = t.sType;
		this.sFrom = t.sFrom;
	}

	/**
	 * @param nStelle
	 * @return
	 */
	public String getMessageStringPart(int nStelle) {
		String s = getMessageStringRawPart(nStelle);
		if (s != null) {
			// Satzzeichen ignorieren
			return s.toLowerCase().replace("/", "").replace(".", "").replace("?", "").replace("!", "").trim();
		}
		return null;
	}

	/**
	 * @param nStelle
	 * @return
	 */
	public String getMessageStringRawPart(int nStelle) {
		try {
			int n = 0;
			for (String part : sMessage.split(" ")) {
				if (n == nStelle) {
					return part.trim();
				}
				n++;
			}
		} catch (Exception e) {
			LOGGER.warn("sgetMessageWord " + e);
		}
		return null;
	}

	/**
	 * @return mgs ohne die ersten zwei wörter
	 */
	public String getMessageStringSecond() {
		String s = sMessage.substring(sMessage.indexOf(' ') + 1);
		return s.substring(s.indexOf(' ')).trim();
	}

	/**
	 * @return mgs ohne das erste wort
	 */
	public String getMessageStringFirst() {
		return sMessage.substring(sMessage.indexOf(' ') + 1).trim();
	}

	/**
	 * @param nStelle
	 * @return
	 */
	public Integer getMessageIntPart(int nStelle) {
		try {
			return Integer.parseInt(getMessageStringPart(nStelle));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param sAntwort
	 */
	public void setAntwort(String sAntwort) {

		this.sAntwort = escapeString(sAntwort);

	}

	// TODO hier löschen
	private String escapeString(String s) {
		return s.replaceAll("\n", "%0A").replaceAll(" ", "%20").replaceAll("\t", "%20").replaceAll("\\|", "%7C")
				.replaceAll("'", "%27").replaceAll("<", "_").replaceAll(">", "_").replaceAll("'", "_")
				.replaceAll("\"", "_").replaceAll("\\{", "(").replaceAll("\\}", ")");
	}

	/**
	 * @return
	 */
	public String getAntwort() {
		if (sAntwort != null) {
			return sAntwort;
		}
		return null;
	}

	public Integer getFromIdOnly() {
		// parse josn
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sFrom);
			return jn.get("id").asInt();
		} catch (Exception e) {
			LOGGER.warn("getFromIdOnly " + e);
			return null;
		}

	}

//	/**
//	 * @param sAntwort
//	 */
//	public void setAntwortOld(String sAntwort) {
//		try {
//			this.sAntwort = URLEncoder.encode(sAntwort, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			this.sAntwort = "FAIL";
//			MLogger.warn("setAntwort" + e);
//		}
//	}

	public void setMessage(String sMessage) {
		// this.sMessage = new MyString(sMessage).getS();
		this.sMessage = sMessage.trim();
	}

	public Integer getMid() {
		return nMid;
	}

	public void setMid(Integer nMid) {
		this.nMid = nMid;
	}

	public Integer getChatID() {
		return nChatID;
	}

	public void setChatID(Integer nChatID) {
		this.nChatID = nChatID;
	}

	public String getMessage() {
		return sMessage;
	}

	public String getType() {
		return sType;
	}

	public void setType(String sType) {
		this.sType = sType;
	}

	public Integer getDate() {
		return nDate;
	}

	public void setDate(Integer nDate) {
		this.nDate = nDate;
	}

	public String getFrom() {
		return sFrom;
	}

	public void setFrom(String sFrom) {
		this.sFrom = sFrom;
	}

	public String getOptions() {
		return sOptions;
	}

	public void setOptions(String sOptions) {
		this.sOptions = sOptions;
	}

}
