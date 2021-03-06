package org.wipf.jasmarty.logic.jasmarty.lcd2004;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.CustomChar;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd2004Cache;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;

@ApplicationScoped
public class Lcd2004 {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Lcd2004PageVerwaltung pageVerwaltung;
	@Inject
	Lcd2004CustomChars customChars;
	@Inject
	Lcd2004CharPictures charPictures;

	private static final Logger LOGGER = Logger.getLogger("Jasmarty 2004");
	private Lcd2004Cache lcache = new Lcd2004Cache(0, 0);
	private boolean bPauseWriteToLCD = false;

	/**
	 * @return
	 * @throws SQLException
	 */
	public Boolean startLCD() throws SQLException {
		LOGGER.info("Starte 2004 LCD");
		pageVerwaltung.initDB();
		customChars.initDB();

		// lcdConnect.startSerialLcdPort(); wird über start2004LCD

		charPictures.writeAndLoadWipf();
		// charPictures.testChars();

		pageVerwaltung.writeStartPage();
		// Cache vorbereiten
		lcache = new Lcd2004Cache(lcdConnect.getWidth(), lcdConnect.getHeight());
		return lcdConnect.startPort();
	}

	/**
	 * 
	 */
	public void stopLCD() {
		pageVerwaltung.writeExitPage();
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
	public Lcd2004Cache getCache() {
		return lcache;
	}

	/**
	 * 
	 */
	public void clearScreen() {
		lcache.clearCacheFull();
		if (lcdConnect.isLcdOk()) {
			lcdConnect.write(254);
			lcdConnect.write(88);
		}
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		if (lcache.hasChanges() && lcdConnect.isLcdOk()) {
			for (int y = 0; y < lcache.getHeight(); y++) {
				for (int x = 0; x < lcache.getWidth(); x++) {
					if (lcache.getCacheIst(x, y) != lcache.getCacheSoll(x, y)) {
						// Schreibe Zeile immer zu ende
						setCursor(x, y);
						for (int writePosX = x; writePosX < lcache.getWidth(); writePosX++) {
							char c = lcache.getCacheSoll(writePosX, y);
							lcdConnect.write(c);
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
	public void setCursor(int x, int y) {
		lcdConnect.write(254);
		lcdConnect.write(71);
		// Arduino 0/0 ist 1/1
		lcdConnect.write(x + 1);
		lcdConnect.write(y + 1);
	}

	/**
	 * @param ca
	 * @param nIndex
	 */
	public void writeCustomChar(CustomChar cc) {
		lcdConnect.write(254);
		lcdConnect.write(21);
		for (int i = 0; i < 8; i++) {
			lcdConnect.write((int) cc.getBytesForLine(i));
		}
		lcdConnect.write(cc.getPosition());
	}

	/**
	 * @return
	 */
	public boolean isLcdOk() {
		return lcdConnect.isLcdOk();
	}

	/**
	 * Wenn der Refesh ausgeschaltet werden soll, wird 2x getRefreshRate gewartet
	 * 
	 * So kann doppeltes schreiben verhindert werden (Custom Chars)
	 * 
	 * @param bPauseWriteToLCD
	 */
	public void setbPauseWriteToLCD(boolean bPauseWriteToLCD) {
		if (bPauseWriteToLCD) {
			try {
				Thread.sleep((long) lcdConnect.getRefreshRate() * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.bPauseWriteToLCD = bPauseWriteToLCD;
	}

	/**
	 * @return
	 */
	public boolean isPauseWriteToLCD() {
		return bPauseWriteToLCD;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return lcdConnect.getHeight();
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return lcdConnect.getWidth();
	}

}
