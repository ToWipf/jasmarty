package org.wipf.wipfapp.logic.jasmarty;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.wipfapp.datatypes.LcdCache;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class JaSmartyConnect {

	private SerialPort sp;
	private LcdCache lc;

	public void writeLineToCache(Integer x, Integer y, String s) {
		lc.writeLine(x, y, s);
	}

	public void writeToCache(Integer x, Integer y, char c) {
		lc.write(x, y, c);
	}

	public String getCachIst() {
		return lc.toStringIst();
	}

	public String getCachSoll() {
		return lc.toStringSoll();
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		System.out.println("in");
		if (lc.hasChanges()) {
			System.out.println("upd");
			for (int y = 0; y < lc.getHight(); y++) {
				for (int x = 0; x < lc.getWidh(); x++) {
					if (lc.getCacheIst(x, y) != lc.getCacheSoll(x, y)) {
						System.out.println("chn in Line:" + y);
						// Schreibe Zeile immer zu ende
						setCursor(x, y);
						for (int writePosX = x; x < lc.getWidh() - writePosX; writePosX++) {
							char c = lc.getCacheSoll(writePosX, y);
							writeChar(c);
							lc.setToCacheIst(writePosX, y, c);
						}
						break;
					}
				}
			}
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
	 * @param c
	 * @return
	 */
	public Boolean writeChar(char c) {
		return writeAscii((int) c);
	}

	/**
	 * @param s
	 */
	public void writeString(String s) {
		for (char ch : s.toCharArray()) {
			writeChar(ch);
		}
	}

	/**
	 * @param s
	 * @return
	 */
	public Boolean writeAscii(Integer n) {
		System.out.println("write: " + n);

		try {
			sp.getOutputStream().write(n.byteValue());
			sp.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public void clearScreen() {
		writeAscii(254);
		writeAscii(88);
	}

	/**
	 * @return
	 */
	public Boolean open() {
		sp = SerialPort.getCommPort("COM10");
		sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

		if (sp.openPort()) {
			System.out.println("Port is open");
		} else {
			System.out.println("Failed to open port");
			return false;
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			;
		}

		clearScreen();

		lc = new LcdCache(20, 4);

		return true;
	}

	/**
	 * @return
	 */
	public Boolean close() {
		if (sp.closePort()) {
			System.out.println("Port is closed");
		} else {
			System.out.println("Failed to close port");
			return false;
		}
		return true;
	}
}
