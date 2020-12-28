package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class LcdConfig {

	public enum lcdType {
		LCD_2004, LCD_12864
	};

	private String sPort;
	private Integer nHeight = 0;
	private Integer nWidth = 0;
	private Integer nBaudRate;
	private int nRefreshRate;
	private lcdType type;

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
		jo.put("type", type.name());
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
		setType(jo.getString("type"));
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

	/**
	 * @return
	 */
	public lcdType getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(lcdType type) {
		this.type = type;
	}

	public void setType(String sType) {
		switch (sType) {
		case "LCD_2004":
			this.type = lcdType.LCD_2004;
			break;
		case "LCD_12864":
			this.type = lcdType.LCD_12864;
			break;
		default:
			this.type = null;
			break;
		}
	}

}
