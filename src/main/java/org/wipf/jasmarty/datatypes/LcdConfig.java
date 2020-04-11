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
	private Integer nheight;
	private Integer nWidth;
	private Integer nBaudRate = 9600;
	private int nRefreshRate = 200;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("port", sPort);
		jo.put("width", nWidth);
		jo.put("height", nheight);
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
			this.nheight = jn.get("height").asInt();
			this.nBaudRate = jn.get("baudrate").asInt();
			this.nRefreshRate = jn.get("refreshrate").asInt();
			return this;
		} catch (Exception e) {
			return null;
		}
	}

	public String getPort() {
		return sPort;
	}

	public void setPort(String sPort) {
		this.sPort = sPort;
	}

	public Integer getHeight() {
		return nheight;
	}

	public void setHeight(Integer nheight) {
		this.nheight = nheight;
	}

	public Integer getWidth() {
		return nWidth;
	}

	public void setWidth(Integer nWidth) {
		this.nWidth = nWidth;
	}

	public Integer getBaudRate() {
		return nBaudRate;
	}

	public void setBaudRate(Integer nBaudRate) {
		this.nBaudRate = nBaudRate;
	}

	public int getRefreshRate() {
		return nRefreshRate;
	}

	public void setRefreshRate(int nRefreshRate) {
		this.nRefreshRate = nRefreshRate;
	}

}
