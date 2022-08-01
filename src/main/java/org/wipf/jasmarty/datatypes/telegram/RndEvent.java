package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class RndEvent {

	private Integer nId;
	private String sEventText;
	private Boolean bActive;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", nId);
		jo.put("eventtext", sEventText);
		jo.put("active", bActive);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public RndEvent setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.sEventText = jo.getString("eventtext");
		this.bActive = jo.getBoolean("active");
		return this;
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
	public void setId(Integer nId) {
		this.nId = nId;
	}

	/**
	 * @return
	 */
	public String getEventText() {
		return sEventText;
	}

	/**
	 * @param sEventText
	 */
	public void setEventText(String sEventText) {
		this.sEventText = sEventText;
	}

	/**
	 * @return
	 */
	public Boolean getActive() {
		return bActive;
	}

	/**
	 * @param bActive
	 */
	public void setActive(Boolean bActive) {
		this.bActive = bActive;
	}

}