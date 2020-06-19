package org.wipf.jasmarty.datatypes;

/**
 * @author wipf
 *
 */
public class TelegramBase {
	private Integer nMid;
	private Integer nChatID;
	private Integer nDate;
	private String sType;
	private String sFrom;

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
