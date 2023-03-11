package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;

/**
 * @author wipf
 *
 */
public class LcdConfig extends ArduinoConfig {

	private Integer nHeight = 0;
	private Integer nWidth = 0;
	private int nRefreshRate;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("port", sPort);
		jo.put("width", nWidth);
		jo.put("height", nHeight);
		jo.put("baudrate", this.nBaudRate);
		jo.put("refreshrate", nRefreshRate);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return
	 */
	public LcdConfig setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);

		setPort(jo.getString("port"));
		setWidth(jo.getInt("width"));
		setHeight(jo.getInt("height"));
		setBaudRate(jo.getInt("baudrate"));
		setRefreshRate(jo.getInt("refreshrate"));
		return this;
	}

	/**
	 * @return
	 */
	public Integer getHeight() {
		return nHeight;
	}

	/**
	 * @param nheight
	 */
	public void setHeight(Integer nheight) {
		this.nHeight = nheight;
	}

	/**
	 * @return
	 */
	public Integer getWidth() {
		return nWidth;
	}

	/**
	 * @param nWidth
	 */
	public void setWidth(Integer nWidth) {
		this.nWidth = nWidth;
	}

	/**
	 * @return
	 */
	public int getRefreshRate() {
		return nRefreshRate;
	}

	/**
	 * @param nRefreshRate
	 */
	public void setRefreshRate(int nRefreshRate) {
		this.nRefreshRate = nRefreshRate;
	}

}
