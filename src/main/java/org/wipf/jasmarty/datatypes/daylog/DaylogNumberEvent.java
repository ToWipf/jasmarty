package org.wipf.jasmarty.datatypes.daylog;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class DaylogNumberEvent {

	private Integer nId;
	private Integer nDateId;
	private String sTyp;
	private Integer nNumber;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("dateid", this.nDateId);
		jo.put("typ", this.sTyp);
		jo.put("number", this.nNumber);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public DaylogNumberEvent setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.nDateId = jo.getInt("dateid");
		this.sTyp = jo.getString("typ");
		this.nNumber = jo.getInt("number");
		return this;
	}

	/**
	 * @return
	 */
	public String getTyp() {
		return sTyp;
	}

	/**
	 * @param sTyp
	 */
	public void setTyp(String sTyp) {
		this.sTyp = sTyp;
	}

	/**
	 * @return
	 */
	public Integer getDateId() {
		return nDateId;
	}

	/**
	 * @param nDateId
	 */
	public void setDateId(Integer nDateId) {
		this.nDateId = nDateId;
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
	public Integer getNumber() {
		return nNumber;
	}

	/**
	 * @param number
	 */
	public void setNumber(Integer number) {
		this.nNumber = number;
	}

}
