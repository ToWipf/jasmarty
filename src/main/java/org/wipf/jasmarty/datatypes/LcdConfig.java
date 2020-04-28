package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf
 *
 */
public class LcdConfig {

	private String sPort;
	private Integer nHeight;
	private Integer nWidth;
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sJson);

			this.sPort = jn.get("port").asText();
			this.nWidth = jn.get("width").asInt();
			this.nHeight = jn.get("height").asInt();
			this.nBaudRate = jn.get("baudrate").asInt();
			this.nRefreshRate = jn.get("refreshrate").asInt();
			return this;
		} catch (Exception e) {
			return null;
		}
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
