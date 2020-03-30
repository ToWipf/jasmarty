package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class Game {
	private Integer nChatID;
	private Integer nDate;
	private String sType;

	public Integer getChatID() {
		return nChatID;
	}

	public void setChatID(Integer nChatID) {
		this.nChatID = nChatID;
	}

	public Integer getDate() {
		return nDate;
	}

	public void setDate(Integer nDate) {
		this.nDate = nDate;
	}

	public String getType() {
		return sType;
	}

	public void setType(String sType) {
		this.sType = sType;
	}

	/**
	 * @param t
	 */
	public void setByTelegram(Telegram t) {
		setChatID(t.getChatID());
		setDate(t.getDate());
		setType(t.getType());
	}
}
