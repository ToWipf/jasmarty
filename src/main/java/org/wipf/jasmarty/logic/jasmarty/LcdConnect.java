package org.wipf.jasmarty.logic.jasmarty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.CustomChar;
import org.wipf.jasmarty.datatypes.LcdCache;
import org.wipf.jasmarty.datatypes.LcdConfig;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class LcdConnect {

	private static final Logger LOGGER = Logger.getLogger("Jasmarty Connect");

	private SerialPort sp;
	private LcdCache lcache;
	private LcdConfig lconf;
	private boolean bLcdIsOk = false;
	private boolean bLed = false;

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
		writeAscii(254);
		writeAscii(66);
	}

	/**
	 * 
	 */
	public void ledOff() {
		this.bLed = false;
		writeAscii(254);
		writeAscii(70);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolUp() {
		writeAscii(254);
		writeAscii(40);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolDown() {
		writeAscii(254);
		writeAscii(41);
	}

	/**
	 * Achtung: Nur mit Arduino Pro-Micro möglich
	 */
	public void commandVolMute() {
		writeAscii(254);
		writeAscii(42);
	}

	/**
	 * @param ca
	 * @param nIndex
	 */
	public void writeCustomChar(CustomChar cc) {
		writeAscii(254);
		writeAscii(21);
		for (int i = 0; i < 8; i++) {
			writeAscii((int) cc.getBytesForLine(i));
		}
		writeAscii(cc.getPosition());
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
	public int getHeight() {
		return lconf.getHeight();
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return lconf.getWidth();
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
	 * @param x
	 * @param y
	 * @param s
	 */
	public void writeLineToCache(Integer x, Integer y, char[] cArr) {
		lcache.writeLine(x, y, cArr);
	}

	/**
	 * @param x
	 * @param y
	 * @param c
	 */
	public void writeCharToCache(Integer x, Integer y, char c) {
		lcache.write(x, y, c);
	}

	/**
	 * @return
	 */
	public LcdCache getCache() {
		return lcache;
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
	 * 
	 */
	public void clearScreen() {
		lcache.clearCacheFull();
		if (isLcdOk()) {
			writeAscii(254);
			writeAscii(88);
		}
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		if (lcache.hasChanges() && isLcdOk()) {
			for (int y = 0; y < lcache.getHeight(); y++) {
				for (int x = 0; x < lcache.getWidth(); x++) {
					if (lcache.getCacheIst(x, y) != lcache.getCacheSoll(x, y)) {
						// Schreibe Zeile immer zu ende
						setCursor(x, y);
						for (int writePosX = x; writePosX < lcache.getWidth(); writePosX++) {
							char c = lcache.getCacheSoll(writePosX, y);
							writeChar(c);
							lcache.setToCacheIst(writePosX, y, c);
						}
						break;
						// x = max
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	public Integer readButton() {
		try {
			char[] in = new char[1];
			BufferedReader input = new BufferedReader(new InputStreamReader(sp.getInputStream()));
			input.read(in);
			return (int) in[0];

		} catch (Exception e) {
			// Kein input,
			return null;
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setCursor(int x, int y) {
		writeAscii(254);
		writeAscii(71);
		// Arduino 0/0 ist 1/1
		writeAscii(x + 1);
		writeAscii(y + 1);
	}

	/**
	 * @param s
	 * @return
	 */
	public void writeAscii(Integer n) {
		try {
			sp.getOutputStream().write(n.byteValue());
			sp.getOutputStream().flush();
		} catch (IOException e) {
			this.bLcdIsOk = false;
			LOGGER.warn("writeAscii: " + e);
		}
	}

	/**
	 * @param c
	 * @return
	 */
	private void writeChar(char c) {
		writeAscii((int) c);
	}

	/**
	 * @return
	 */
	private Boolean startPort() {
		// Cache vorbereiten
		lcache = new LcdCache(lconf.getWidth(), lconf.getHeight());

		try {
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
