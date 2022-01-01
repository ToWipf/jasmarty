package org.wipf.jasmarty.datatypes.daylog;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class DaylogType {

	private Integer nId;
	private String sType;
	private String sArt;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("type", this.sType);
		jo.put("art", this.sArt);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public DaylogType setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.sType = jo.getString("type");
		this.sArt = jo.getString("art");
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
	public String getArt() {
		return sArt;
	}

	/**
	 * @param sArt
	 */
	public void setArt(String sArt) {
		this.sArt = sArt;
	}

}
