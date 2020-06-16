package org.wipf.jasmarty.datatypes;

/**
 * @author wipf
 *
 */
public class Game {
	private Integer nChatID;
	private Integer nDate;
	private String sType;

	/**
	 * @param t
	 */
	public void setByTelegram(Telegram t) {
		setChatID(t.getChatID());
		setDate(t.getDate());
		setType(t.getType());
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
	public String getType() {
		return sType;
	}

	/**
	 * @param sType
	 */
	public void setType(String sType) {
		this.sType = sType;
	}

}
