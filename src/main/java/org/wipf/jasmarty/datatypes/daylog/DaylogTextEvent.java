package org.wipf.jasmarty.datatypes.daylog;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class DaylogTextEvent {

	private Integer nId;
	private Integer nDateId;
	private String sTyp;
	private String sText;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("dateid", this.nDateId);
		jo.put("typ", this.sTyp);
		jo.put("text", this.sText);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public DaylogTextEvent setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.nDateId = jo.getInt("dateid");
		this.sTyp = jo.getString("typ");
		this.sText = jo.getString("text");
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
	public String getText() {
		return sText;
	}

	/**
	 * @param sText
	 */
	public void setText(String sText) {
		this.sText = sText;
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

}
