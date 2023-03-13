package org.wipf.jasmarty.logic.lcd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class LcdConnect {

	private static final Logger LOGGER = Logger.getLogger("Jasmarty Connect");

	private SerialPort sp;
	private LcdConfig lconf = new LcdConfig();
	private boolean bLed = false;
	private boolean bLcdIsOk = false;

	/**
	 * 
	 */
	public void ledToggle() {
		if (bLed) {
			ledOff();
		} else {
			ledOn();
		}
	}

	/**
	 * 
	 */
	public void ledOn() {
		this.bLed = true;
		write(254);
		write(66);
	}

	/**
	 * 
	 */
	public void ledOff() {
		this.bLed = false;
		write(254);
		write(70);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolUp() {
		write(254);
		write(40);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolDown() {
		write(254);
		write(41);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolMute() {
		write(254);
		write(42);
	}

	/**
	 * @param lconfig
	 */
	public void setConfig(LcdConfig lconfig) {
		this.lconf = lconfig;
	}

	/**
	 * @return
	 */
	public boolean isLcdOk() {
		return bLcdIsOk;
	}

	/**
	 * @return
	 */
	public int getRefreshRate() {
		return this.lconf.getRefreshRate();
	}

	/**
	 * @return
	 */
	public Boolean startSerialLcdPort() {
		if (!bLcdIsOk) {
			if (startPort()) {
				LOGGER.info("Port " + lconf.getPort() + " geöffnet");
				return true;
			} else {
				LOGGER.warn("Port " + lconf.getPort() + " nicht geöffnet!");
				return false;
			}
		} else {
			LOGGER.info("Port " + lconf.getPort() + " bereits geöffnet");
			return true;
		}
	}

	/**
	 * @return
	 */
	public Boolean closeSerialLcdPort() {
		// refresh ausschalten
		bLcdIsOk = false;
		if (sp.closePort()) {
			LOGGER.info("Port " + lconf.getPort() + " geschlossen");
			return true;
		} else {
			LOGGER.warn("Port " + lconf.getPort() + " nicht geschlossen!");
			return false;
		}
	}

	/**
	 * @return
	 */
	public Integer readButton() {
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
	 * @param c
	 * @return
	 */
	public void write(char c) {
		write((int) c);
	}

	/**
	 * @param b
	 */
	public void write(byte b) {
		write((int) b);
	}

	/**
	 * @param s
	 * @return
	 */
	@Metered
	public void write(Integer n) {
		try {
			sp.getOutputStream().write(n.byteValue());
			sp.getOutputStream().flush();
		} catch (IOException e) {
			this.bLcdIsOk = false;
			LOGGER.warn("writeAscii: " + e);
		}
	}

	/**
	 * @return
	 */
	public Boolean startPort() {
		try {
			if (lconf.getPort() == null || lconf.getPort().equals("")) {
				LOGGER.warn("LCD USB Port ist nicht definiert");
				return false;
			}
			// LCD Connect
			sp = null;
			sp = SerialPort.getCommPort(lconf.getPort());
			sp.setComPortParameters(lconf.getBaudRate(), 8, 1, 0); // default connection settings for Arduino
			sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

			if (!sp.openPort()) {
				LOGGER.warn("LCD Port " + lconf.getPort() + " nicht gefunden");
				return false;
			} else {
				// Warten bis LCD bereit ist
				bLcdIsOk = true;
				Thread.sleep(5000);
				// clearScreen(); nicht nötig
				return true;
			}

		} catch (Exception e) {
			LOGGER.warn("LCD Start: " + e);
			return false;
		}
	}

}
