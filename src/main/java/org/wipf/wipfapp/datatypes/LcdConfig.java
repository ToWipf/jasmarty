package org.wipf.wipfapp.datatypes;

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

}
