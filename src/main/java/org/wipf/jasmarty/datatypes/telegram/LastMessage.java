package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class LastMessage {

	private Integer nChatId;
	private String sMsg;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("userid", this.nChatId);
		jo.put("date", this.sMsg);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public LastMessage setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		this.nChatId = jo.getInt("userid");
		this.sMsg = jo.getString("date");
		return this;
	}

	/**
	 * @return
	 */
	public Integer getChatId() {
		return nChatId;
	}

	/**
	 * @param userId
	 */
	public void setChatId(Integer userId) {
		this.nChatId = userId;
	}

	/**
	 * @return
	 */
	public String getMsg() {
		return sMsg;
	}

	/**
	 * @param date
	 */
	public void setMsg(String date) {
		this.sMsg = date;
	}

}
