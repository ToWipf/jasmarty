package org.wipf.jasmarty.datatypes.eisenbahn;

/**
 * @author Wipf
 *
 */
public class ArduinoConfig {

	protected String sPort;
	protected Integer nBaudRate;
	private Integer nLineLength;

	/**
	 * @return
	 */
	public boolean isValid() {
		return (sPort != null && nBaudRate != null && nLineLength != null && nBaudRate > 1 && nLineLength > 0);
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
	public Integer getLineLength() {
		return nLineLength;
	}

	/**
	 * @param nLineLength
	 */
	public void setLineLength(Integer nLineLength) {
		this.nLineLength = nLineLength;
	}
}
