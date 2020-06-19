package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class TeleMsg extends TelegramBase {

	private String sMessage;
	private String sAntwort;
	private String sOptions;

	/**
	 * 
	 */
	public TeleMsg() {
		setMid(0);
		this.sMessage = "";
	}

	/**
	 * @param copy of t
	 */
	public TeleMsg(TeleMsg t) {
		this.sMessage = t.sMessage;
		this.sAntwort = t.sAntwort;
		setChatID(t.getChatID());
		setMid(t.getMid());
		setDate(t.getDate());
		setType(t.getType());
		setFrom(t.getFrom());
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
		JSONObject jo = new JSONObject(getFrom());
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
	public String getMessage() {
		return sMessage;
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
