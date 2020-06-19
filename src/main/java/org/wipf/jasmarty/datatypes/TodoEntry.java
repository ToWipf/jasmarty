package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class TodoEntry extends Telegram {

	private String sData;
	private String sActive;
	private Integer nId;
	private String sRemind;
	private String sEditBy;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = this.toJsonTelegram();
		jo.put("data", this.sData);
		jo.put("active", this.sActive);
		jo.put("id", this.nId);
		jo.put("remind", this.sRemind);
		jo.put("editby", this.sEditBy);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public TodoEntry setByJson(String sJson) {
		try {
			this.setByTelegram(this.setByJsonTelegram(sJson));

			JSONObject jo = new JSONObject(sJson);

			this.setEditBy(jo.getString("editby"));
			this.sData = jo.getString("data");
			this.sActive = jo.getString("active");
			this.nId = jo.getInt("id");
			this.sRemind = jo.getString("remind");

			return this;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return via From
	 */
	public String getEditBy() {
		return this.sEditBy;
	}

	/**
	 * @param sEditBy
	 * @return
	 */
	public void setEditBy(String sEditBy) {
		this.sEditBy = sEditBy;
	}

	/**
	 * @return
	 */
	public String getData() {
		return sData;
	}

	/**
	 * @param sData
	 */
	public void setData(String sData) {
		this.sData = sData;
	}

	/**
	 * @return
	 */
	public String getActive() {
		return sActive;
	}

	/**
	 * @param sActive
	 */
	public void setActive(String sActive) {
		this.sActive = sActive;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return nId;
	}

	/**
	 * @param nId
	 */
	public void setId(int nId) {
		this.nId = nId;
	}

	/**
	 * @return
	 */
	public String getRemind() {
		return sRemind;
	}

	/**
	 * @param sRemind
	 */
	public void setRemind(String sRemind) {
		this.sRemind = sRemind;
	}

}
