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
	public LcdCache lc;

	/**
	 * 
	 */
	public void refreshDisplay() {
		if (lc.hasChanges()) {
			for (int y = 1; y <= lc.getHight(); y++) {
				for (int x = 1; x <= lc.getWidh(); x++) {
					if (lc.getCacheOld(x, y) != lc.getCacheNew(x, y)) {
						// Schreibe Zeile immer zu ende
						setCursor(x, y);
						for (int writePosX = x; x < lc.getWidh(); writePosX++) {
							char c = lc.getCacheNew(writePosX, y);
							writeChar(c);
							lc.setToCacheOld(writePosX, y, c);
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
		writeAscii(x);
		writeAscii(y);
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
