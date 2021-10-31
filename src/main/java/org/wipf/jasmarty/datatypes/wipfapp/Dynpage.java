package org.wipf.jasmarty.datatypes.wipfapp;

import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
public class Dynpage {

	private Integer nPageId;
	private String sHtml;
	private String sScript;
	private String sStyle;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("pageid", this.nPageId);
		jo.put("html", this.sHtml);
		jo.put("script", this.sScript);
		jo.put("style", this.sStyle);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public Dynpage setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nPageId = jo.getInt("pageid");
		}
		this.sHtml = jo.getString("html");
		this.sScript = jo.getString("script");
		this.sStyle = jo.getString("style");
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

	/**
	 * @return
	 */
	public Integer getPageId() {
		return nPageId;
	}

	/**
	 * @param nPageId
	 */
	public void setPageId(Integer nPageId) {
		this.nPageId = nPageId;
	}

}
