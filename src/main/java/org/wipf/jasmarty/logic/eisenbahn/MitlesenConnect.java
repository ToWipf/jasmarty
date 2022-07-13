package org.wipf.jasmarty.logic.eisenbahn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class MitlesenConnect {

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen Connect");

	private SerialPort sp;
	private ArduinoConfig aConf = new ArduinoConfig();
	private boolean bConnectOk = false;

	/**
	 * @param aConf
	 */
	public void setConfig(ArduinoConfig aConf) {
		this.aConf = aConf;
	}

	/**
	 * @return
	 */
	public Boolean startSerialLcdPort() {
		if (!bConnectOk) {
			if (startPort()) {
				LOGGER.info("Port " + aConf.getPort() + " geöffnet");
				return true;
			} else {
				LOGGER.warn("Port " + aConf.getPort() + " nicht geöffnet!");
				return false;
			}
		} else {
			LOGGER.info("Port " + aConf.getPort() + " bereits geöffnet");
			return true;
		}
	}

	/**
	 * @return
	 */
	public Boolean closeSerialLcdPort() {

		if (sp.closePort()) {
			LOGGER.info("Port " + aConf.getPort() + " geschlossen");
			return true;
		} else {
			LOGGER.warn("Port " + aConf.getPort() + " nicht geschlossen!");
			return false;
		}
	}

	/**
	 * @return
	 */
	public Integer readInput() {
		char[] in = new char[1];

		try (BufferedReader input = new BufferedReader(new InputStreamReader(sp.getInputStream()))) {
			input.read(in);
			return (int) in[0];
		} catch (IOException e) {
			// Kein input
			return null;
		}
	}

	/**
	 * @return
	 */
	public Boolean startPort() {
		try {
			if (aConf.getPort() == null || aConf.getPort().equals("")) {
				LOGGER.warn("LCD USB Port ist nicht definiert");
				return false;
			}
			// Arduino Connect
			sp = null;
			sp = SerialPort.getCommPort(aConf.getPort());
			sp.setComPortParameters(aConf.getBaudRate(), 8, 1, 0); // default connection settings for Arduino
			sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

			if (!sp.openPort()) {
				LOGGER.warn("LCD Port " + aConf.getPort() + " nicht gefunden");
				return false;
			} else {
				// Warten bis Arduino bereit ist
				bConnectOk = true;
				Thread.sleep(5000);
				return true;
			}

		} catch (Exception e) {
			LOGGER.warn("LCD Start: " + e);
			return false;
		}
	}

}
