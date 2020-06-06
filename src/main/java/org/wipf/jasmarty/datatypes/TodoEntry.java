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
	private Integer nDate;
	private String sActive;
	private Integer nId;
	private String sRemind;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("data", sData);
		jo.put("editby", sEditBy);
		jo.put("date", nDate);
		jo.put("active", sActive);
		jo.put("id", nId);
		jo.put("remind", sRemind);
		return jo;
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
			this.nDate = jn.get("date").asInt();
			this.sActive = jn.get("active").asText();
			this.nId = jn.get("id").asInt();
			this.sRemind = jn.get("remind").asText();
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
	public int getDate() {
		return nDate;
	}

	/**
	 * @param sDate
	 */
	public void setDate(int nDate) {
		this.nDate = nDate;
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

	public String getRemind() {
		return sRemind;
	}

	public void setRemind(String sRemind) {
		this.sRemind = sRemind;
	}

}
