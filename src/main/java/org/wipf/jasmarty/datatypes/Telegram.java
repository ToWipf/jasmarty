package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class Telegram {

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
		int n = 0;
		for (String part : this.sMessage.split(" ")) {
			if (n == nStelle) {
				return part.trim();
			}
			n++;
		}
		// Wenn nichts gefunden wird
		return null;
	}

	/**
	 * @return mgs ohne die ersten zwei wörter
	 */
	public String getMessageFullWithoutSecondWord() {
		String s = getMessageFullWithoutFirstWord();
		return s.substring(s.indexOf(' ')).trim();
	}

	/**
	 * @return mgs ohne das erste wort
	 */
	public String getMessageFullWithoutFirstWord() {
		return this.sMessage.substring(this.sMessage.indexOf(' ') + 1).trim();
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
	 * @return
	 */
	public Integer getFromIdOnly() {
		JSONObject jo = new JSONObject(sFrom);
		return jo.getInt("id");
	}

	/**
	 * @param sMessage
	 */
	public void setMessage(String sMessage) {
		this.sMessage = sMessage;
	}

	/**
	 * @return
	 */
	public Integer getMid() {
		return nMid;
	}

	/**
	 * @param nMid
	 */
	public void setMid(Integer nMid) {
		this.nMid = nMid;
	}

	/**
	 * @return
	 */
	public Integer getChatID() {
		return nChatID;
	}

	/**
	 * @param nChatID
	 */
	public void setChatID(Integer nChatID) {
		this.nChatID = nChatID;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return sMessage;
	}

	/**
	 * @return
	 */
	public String getType() {
		return sType;
	}

	/**
	 * @param sType
	 */
	public void setType(String sType) {
		this.sType = sType;
	}

	/**
	 * @return
	 */
	public Integer getDate() {
		return nDate;
	}

	/**
	 * @param nDate
	 */
	public void setDate(Integer nDate) {
		this.nDate = nDate;
	}

	/**
	 * @return
	 */
	public String getFrom() {
		return sFrom;
	}

	/**
	 * @param sFrom
	 */
	public void setFrom(String sFrom) {
		this.sFrom = sFrom;
	}

	/**
	 * @return
	 */
	public String getOptions() {
		return sOptions;
	}

	/**
	 * @param sOptions
	 */
	public void setOptions(String sOptions) {
		this.sOptions = sOptions;
	}

	/**
	 * @param sAntwort
	 */
	public void setAntwort(String sAntwort) {
		this.sAntwort = sAntwort;
	}

	/**
	 * @return
	 */
	public String getAntwort() {
		return sAntwort;
	}

}
