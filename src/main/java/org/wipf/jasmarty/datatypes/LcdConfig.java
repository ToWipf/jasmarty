package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class LcdConfig {

	private String sPort;
	private Integer nHeight = 0;
	private Integer nWidth = 0;
	private Integer nBaudRate;
	private int nRefreshRate;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("port", sPort);
		jo.put("width", nWidth);
		jo.put("height", nHeight);
		jo.put("baudrate", nBaudRate);
		jo.put("refreshrate", nRefreshRate);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return
	 */
	public LcdConfig setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);

		this.sPort = jo.getString("port");
		this.nWidth = jo.getInt("width");
		this.nHeight = jo.getInt("height");
		this.nBaudRate = jo.getInt("baudrate");
		this.nRefreshRate = jo.getInt("refreshrate");
		return this;
	}

	/**
	 * @return
	 */
	public String getPort() {
		return sPort;
	}

	/**
	 * @param sPort
	 */
	public void setPort(String sPort) {
		this.sPort = sPort;
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
	public Integer getBaudRate() {
		return nBaudRate;
	}

	/**
	 * @param nBaudRate
	 */
	public void setBaudRate(Integer nBaudRate) {
		this.nBaudRate = nBaudRate;
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
