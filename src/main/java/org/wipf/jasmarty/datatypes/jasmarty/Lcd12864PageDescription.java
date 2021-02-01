package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class Lcd12864PageDescription {

	private int nId;
	private String sName;
	private int timeoutTime;
	private JSONArray jaStatic;
	private JSONArray jaDynamic;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", getId());
		jo.put("name", getName());
		jo.put("timeouttime", getTimeouttime());
		jo.put("static", getStatic());
		jo.put("dynamic", getDynamic());
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Lcd12864PageDescription setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		setId(jo.getInt("id"));
		setName(jo.getString("name"));
		setTimeouttime(jo.getInt("timeouttime"));
		setStatic(jo.getJSONArray("static"));
		setDynamic(jo.getJSONArray("dynamic"));
		return this;
	}

	/**
	 * @return
	 */
	public String getName() {
		return sName;
	}

	/**
	 * @param sName
	 */
	public void setName(String sName) {
		this.sName = sName;
	}

	/**
	 * @return
	 */
	public int getId() {
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
	public JSONArray getStatic() {
		return jaStatic;
	}

	/**
	 * @param jaStatic
	 */
	public void setStatic(JSONArray jaStatic) {
		this.jaStatic = jaStatic;
	}

	/**
	 * @param jaStatic
	 */
	public void setStatic(String sStatic) {
		setStatic(new JSONArray(sStatic));
	}

	/**
	 * @return
	 */
	public JSONArray getDynamic() {
		return jaDynamic;
	}

	/**
	 * @param jaDynamic
	 */
	public void setDynamic(JSONArray jaDynamic) {
		this.jaDynamic = jaDynamic;
	}

	/**
	 * @param sDynamic
	 */
	public void setDynamic(String sDynamic) {
		setDynamic(new JSONArray(sDynamic));
	}

	/**
	 * @return
	 */
	public int getTimeouttime() {
		return this.timeoutTime;
	}

	/**
	 * @param nTime
	 */
	public void setTimeouttime(int nTime) {
		this.timeoutTime = nTime;
	}

}
