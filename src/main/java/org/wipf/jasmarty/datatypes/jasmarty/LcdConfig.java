package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;

/**
 * @author wipf
 *
 */
public class LcdConfig extends ArduinoConfig {

	private int nRefreshRate;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("port", sPort);
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
		setBaudRate(jo.getInt("baudrate"));
		setRefreshRate(jo.getInt("refreshrate"));
		return this;
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
