package org.wipf.wipfapp.logic.jasmarty;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.LcdCache;
import org.wipf.wipfapp.datatypes.LcdConfig;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class JaSmartyConnect {

	public static char BLOCK_0_3 = '_';
	public static char BLOCK_1_3 = 0x02;
	public static char BLOCK_2_3 = 0x03;
	public static char BLOCK_3_3 = 0xFF;

	private static final Logger LOGGER = Logger.getLogger("jasmarty Connect");

	private SerialPort sp;
	private LcdCache lcache;
	private LcdConfig lconf;
	private boolean bLcdIsOk = false;

	/**
	 * @return
	 */
	public int getHight() {
		return lconf.getHight();
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return lconf.getWidth();
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
	 * 
	 */
	public void resetLcdOK() {
		bLcdIsOk = true;
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
	public String getCachIstAsString() {
		return lcache.toStringIst();
	}

	/**
	 * @return
	 */
	public String getCachSollAsString() {
		return lcache.toStringSoll();
	}

	/**
	 * @return
	 */
	public Boolean startPort() {
		try {
			// Cache vorbereiten
			lcache = new LcdCache(lconf.getWidth(), lconf.getHight());

			// LCD Connect
			sp = SerialPort.getCommPort(lconf.getPort());
			sp.setComPortParameters(lconf.getBaudRate(), 8, 1, 0); // default connection settings for Arduino
			sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

			if (!sp.openPort()) {
				LOGGER.warn("LCD Port nicht gefunden");
				return false;
			}

			// Warten bis LCD bereit ist
			bLcdIsOk = true;
			Thread.sleep(5000);
			// clearScreen(); nicht nötig
			return true;

		} catch (Exception e) {
			LOGGER.warn("LCD Start: " + e);
			return false;
		}
	}

	/**
	 * @return
	 */
	public Boolean close() {
		// refresh ausschalten
		bLcdIsOk = false;
		return (sp.closePort());
	}

	/**
	 * 
	 */
	public void clearScreen() {
		lcache.clearCacheFull();
		writeAscii(254);
		writeAscii(88);
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		if (lcache.hasChanges()) {
			for (int y = 0; y < lcache.getHight(); y++) {
				for (int x = 0; x < lcache.getWidh(); x++) {
					if (lcache.getCacheIst(x, y) != lcache.getCacheSoll(x, y)) {
						// Schreibe Zeile immer zu ende
						setCursor(x, y);
						for (int writePosX = x; writePosX < lcache.getWidh(); writePosX++) {
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
	 * @param x
	 * @param y
	 */
	private void setCursor(int x, int y) {
		writeAscii(254);
		writeAscii(71);
		// Arduino 0/0 ist 1/1
		writeAscii(x + 1);
		writeAscii(y + 1);
	}

	/**
	 * @param c
	 * @return
	 */
	private void writeChar(char c) {
		writeAscii((int) c);
	}

	/**
	 * @param s
	 * @return
	 */
	private void writeAscii(Integer n) {
		System.out.println("write: " + n);

		try {
			sp.getOutputStream().write(n.byteValue());
			sp.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.bLcdIsOk = false;
			e.printStackTrace();
		}
	}

}
