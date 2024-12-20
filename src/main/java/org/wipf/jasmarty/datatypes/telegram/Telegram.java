package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class Telegram {

	private Integer nMid; // Die Message Id
	private Long nChatID;
	private Integer nDate;
	private String sType;
	private String sFrom;
	private String sMessage;
	private String sAntwort;
	private String sOptions;

	/**
	 * 
	 */
	public Telegram() {
		setMid(0);
		this.sMessage = "";
	}

	/**
	 * @param copy of t
	 */
	public Telegram(Telegram t) {
		setMessage(t.sMessage);
		setAntwort(t.sAntwort);
		setChatID(t.getChatID());
		setMid(t.getMid());
		setDate(t.getDate());
		setType(t.getType());
		setFrom(t.getFrom());
		setOptions(t.getOptions());
	}

	/**
	 * @param copy of t
	 */
	public void setByTelegram(Telegram t) {
		setMessage(t.sMessage);
		setAntwort(t.sAntwort);
		setChatID(t.getChatID());
		setMid(t.getMid());
		setDate(t.getDate());
		setType(t.getType());
		setFrom(t.getFrom());
		setOptions(t.getOptions());
	}

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("mid", getMid());
		jo.put("from", getFrom());
		jo.put("date", getDate());
		jo.put("chatid", getChatID());
		jo.put("type", getType());
		jo.put("message", getMessage());
		jo.put("antwort", getAntwort());
		jo.put("options", getOptions());
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Telegram setByJsonTelegram(String sJson) {
		JSONObject jo = new JSONObject(sJson);

		if (jo.has("chatid")) {
			this.setChatID(jo.getLong("chatid"));
		}

		if (jo.has("type")) {
			this.setType(jo.getString("type"));
		}

		if (jo.has("message")) {
			this.setMessage(jo.getString("message").trim());
		}

		if (jo.has("from")) {
			this.setFrom(jo.getString("from"));
		}

		if (jo.has("date")) {
			this.setDate(jo.getInt("date"));
		}

		if (jo.has("antwort")) {
			this.setAntwort(jo.getString("antwort").trim());
		}

		if (jo.has("options")) {
			this.setOptions(jo.getString("options"));
		}

		if (jo.has("mid")) {
			this.setMid(jo.getInt("mid"));
		}

		return this;
	}

	/**
	 * gibt den ersten String zurück, der im Menü verarbeitet wird
	 * 
	 * @return
	 */
	public String getBeginnStringFromMessage() {
		// Nachrichten mit Links speziell behandeln
		if (this.sMessage.contains("https://")) {
			return "to-link";
		}

		String sIn = getMessageStringPartLow(0);

		if (sIn != null && !sIn.isEmpty()) {
			String[] lines = sIn.split("\n");
			if (lines.length > 0) {
				return lines[0];
			}
		}
		return null;
	}

	/**
	 * @param nStelle
	 * @return
	 */
	public String getMessageStringPartLow(int nStelle) {
		int n = 0;
		// Split by Space
		for (String part : this.sMessage.split(" ")) {
			if (n == nStelle) {
				return part.trim().toLowerCase();
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
			return Integer.parseInt(getMessageStringPartLow(nStelle));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public Integer getFromIdOnly() {
		try {
			JSONObject jo = new JSONObject(getFrom());
			return jo.getInt("id");
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @return
	 */
	public Integer getDate() {
		if (this.nDate == null || this.nDate == 0) {
			this.nDate = ((int) (System.currentTimeMillis() / 1000));
		}
		return this.nDate;
	}

	/**
	 * @param sMessage
	 */
	public void setMessage(String sMessage) {
		// Schneide ein führendes '/' weg
		if (sMessage != null && sMessage.length() > 1) {
			if (sMessage.charAt(0) == '/') {
				sMessage = sMessage.substring(1);
			}
		}
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

	/**
	 * @return
	 */
	public Long getChatID() {
		return nChatID;
	}

	/**
	 * @param nChatID
	 */
	public void setChatID(Long nChatID) {
		this.nChatID = nChatID;
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
	 * @return Message id TODO auf LONG
	 */
	public Integer getMid() {
		return nMid;
	}

	/**
	 * @param Message id
	 */
	public void setMid(Integer nMid) {
		this.nMid = nMid;
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

	@Override
	public String toString() {
		return this.sMessage + " | " + this.sAntwort + " | " + this.sFrom;
	}

}
