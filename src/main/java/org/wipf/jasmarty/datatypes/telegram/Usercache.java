package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class Usercache {

	private Integer nChatId;
	private String sMsg;
	private String sUsercache;
	private Integer nCounter;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("chatid", this.nChatId);
		jo.put("msg", this.sMsg);
		jo.put("usercache", this.sUsercache);
		jo.put("counter", this.nCounter);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Usercache setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		this.nChatId = jo.getInt("chatid");
		this.sMsg = jo.getString("msg");
		this.sUsercache = jo.getString("usercache");
		this.nCounter = jo.getInt("counter");
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
		if (sUsercache == null) {
			return "";
		}
		return sUsercache;
	}

	/**
	 * @param sUsercache
	 */
	public void setUsercache(String sUsercache) {
		this.sUsercache = sUsercache;
	}

	/**
	 * @return
	 */
	public Integer getCounter() {
		return nCounter;
	}

	/**
	 * @param nCounter
	 */
	public void setCounter(Integer nCounter) {
		this.nCounter = nCounter;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "Chat Id: '" + this.nChatId + "'\nLetzte Nachricht: '" + this.sMsg + "'\nCache:'" + this.sUsercache
				+ "'\nCounter: '" + this.nCounter + "'";
	}

}
