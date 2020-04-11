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
	private Integer nHight;
	private Integer nWidth;
	private Integer nBaudRate = 9600;
	private int nRefreshRate = 200;

	public String getPort() {
		return sPort;
	}

	public void setPort(String sPort) {
		this.sPort = sPort;
	}

	public Integer getHight() {
		return nHight;
	}

	public void setHight(Integer nHight) {
		this.nHight = nHight;
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

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("port", sPort);
		jo.put("width", nWidth);
		jo.put("hight", nHight);
		jo.put("baudrate", nBaudRate);
		jo.put("refreshrate", nRefreshRate);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return ok
	 */
	public boolean setByJson(String sJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sJson);

			this.sPort = jn.get("port").asText();
			this.nWidth = jn.get("width").asInt();
			this.nHight = jn.get("hight").asInt();
			this.nBaudRate = jn.get("baudrate").asInt();
			this.nRefreshRate = jn.get("refreshrate").asInt();
			return true;

		} catch (Exception e) {
			return false;
		}
	}

}
