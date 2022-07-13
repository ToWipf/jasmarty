package org.wipf.jasmarty.datatypes.eisenbahn;

/**
 * @author Wipf
 *
 */
public class ArduinoConfig {

	protected String sPort;
	protected Integer nBaudRate;

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
	public boolean isValid() {
		return (sPort != null && nBaudRate > 1);
	}
}
