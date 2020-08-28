package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class Telegram {

	private Integer nMid; // Die Message Id
	private Integer nChatID;
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
	public JSONObject toJsonTelegram() {
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
		try {
			JSONObject jo = new JSONObject(sJson);
			this.setMid(jo.getInt("editby"));
			this.setFrom(jo.getString("from"));
			this.setDate(jo.getInt("date"));
			this.setChatID(jo.getInt("chatid"));
			this.setType(jo.getString("type"));
			this.setMessage(jo.getString("message"));
			this.setAntwort(jo.getString("antwort"));
			this.setOptions(jo.getString("options"));

			System.out.println(jo.toString());

		} catch (Exception e) {
			System.out.println("setByJsonTelegram " + e);
		}
		return this;
	}

	/**
	 * @param nStelle
	 * @return
	 */
	public String getMessageStringPartLow(int nStelle) {
		int n = 0;
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
			return null;
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
	 * @return Message id
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

}
