package org.wipf.jasmarty.datatypes.liste;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class Liste {

	private Integer nId;
	private String sData;
	private Integer nTypeId;
	private Integer nDate;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", nId);
		jo.put("data", sData);
		jo.put("typeid", nTypeId);
		jo.put("date", nDate);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Liste setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.sData = jo.getString("data");
		this.nTypeId = jo.getInt("typeid");
		this.nDate = jo.getInt("date");
		return this;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return nId;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.nId = id;
	}

	/**
	 * @return
	 */
	public String getData() {
		return sData;
	}

	/**
	 * @param data
	 */
	public void setData(String data) {
		this.sData = data;
	}

	/**
	 * @return
	 */
	public Integer getTypeId() {
		return nTypeId;
	}

	/**
	 * @param typeId
	 */
	public void setTypeId(Integer typeId) {
		this.nTypeId = typeId;
	}

	/**
	 * @return
	 */
	public Integer getDate() {
		return nDate;
	}

	/**
	 * @param date
	 */
	public void setDate(Integer date) {
		this.nDate = date;
	}

}
