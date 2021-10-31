package org.wipf.jasmarty.datatypes.wipfapp;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class Dynpage {

	private Integer nId;
	private String sHtml;
	private String sScript;
	private String sStyle;
	private String sRechte;
	private Boolean bLive;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("html", this.sHtml);
		jo.put("script", this.sScript);
		jo.put("style", this.sStyle);
		jo.put("rechte", this.sRechte);
		jo.put("live", this.bLive);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Dynpage setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		this.sHtml = jo.getString("html");
		this.sScript = jo.getString("script");
		this.sStyle = jo.getString("style");
		this.sRechte = jo.getString("rechte");
		this.bLive = jo.getBoolean("live");
		return this;
	}

	/**
	 * @return
	 */
	public String getHtml() {
		return sHtml;
	}

	/**
	 * @param sHtml
	 */
	public void setHtml(String sHtml) {
		this.sHtml = sHtml;
	}

	/**
	 * @return
	 */
	public String getScript() {
		return sScript;
	}

	/**
	 * @param sScript
	 */
	public void setScript(String sScript) {
		this.sScript = sScript;
	}

	/**
	 * @return
	 */
	public String getStyle() {
		return sStyle;
	}

	/**
	 * @param sStyle
	 */
	public void setStyle(String sStyle) {
		this.sStyle = sStyle;
	}

	public Integer getId() {
		return nId;
	}

	public void setId(Integer nId) {
		this.nId = nId;
	}

	public Boolean getLive() {
		return bLive;
	}

	public void setLive(Boolean bLive) {
		this.bLive = bLive;
	}

	public String getRechte() {
		return sRechte;
	}

	public void setRechte(String sRechte) {
		this.sRechte = sRechte;
	}

}
