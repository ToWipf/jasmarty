package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf
 *
 */
public class TodoEntry {

	private String sData;
	private String sEditBy;
	private String sDate;
	private String sActive;
	private Integer nId;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("data", sData);
		jo.put("editby", sEditBy);
		jo.put("date", sDate);
		jo.put("active", sActive);
		jo.put("id", nId);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return
	 */
	public TodoEntry setByJson(String sJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sJson);

			this.sData = jn.get("data").asText();
			this.sEditBy = jn.get("editby").asText();
			this.sDate = jn.get("date").asText();
			this.sActive = jn.get("active").asText();
			this.nId = jn.get("id").asInt();
			return this;
		} catch (Exception e) {
			return null;
		}
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
	public String getEditBy() {
		return sEditBy;
	}

	/**
	 * @param sEditBy
	 */
	public void setEditBy(String sEditBy) {
		this.sEditBy = sEditBy;
	}

	/**
	 * @return
	 */
	public String getDate() {
		return sDate;
	}

	/**
	 * @param sDate
	 */
	public void setDate(String sDate) {
		this.sDate = sDate;
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

}
