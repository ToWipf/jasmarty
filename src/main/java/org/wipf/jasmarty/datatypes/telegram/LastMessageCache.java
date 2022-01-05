package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class LastMessageCache {

	private Integer nChatId;
	private String sMsg;
	private String sUsercache;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("userid", this.nChatId);
		jo.put("date", this.sMsg);
		jo.put("usercache", this.sUsercache);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public LastMessageCache setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		this.nChatId = jo.getInt("chatid");
		this.sMsg = jo.getString("date");
		this.sUsercache = jo.getString("usercache");
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

	/**
	 * @return
	 */
	public String getUsercache() {
		return sUsercache;
	}

	/**
	 * @param sUsercache
	 */
	public void setUsercache(String sUsercache) {
		this.sUsercache = sUsercache;
	}

}
