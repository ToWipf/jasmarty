package org.wipf.jasmarty.datatypes.liste;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class ListeType {

	private Integer nId;
	private String sTypename;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", nId);
		jo.put("typename", sTypename);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public ListeType setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.sTypename = jo.getString("typename");
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
	public String getTypename() {
		return sTypename;
	}

	/**
	 * @param sTypename
	 */
	public void setTypename(String sTypename) {
		this.sTypename = sTypename;
	}
}
