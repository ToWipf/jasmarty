package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864 {

	private static final Logger LOGGER = Logger.getLogger("Jasmarty 12864");

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	Wipf wipf;

	/**
	 * zu übertragen sind 128x64 bit = 8192 bits = 1024 byte
	 * 
	 * pro übertragen 8 bit = 1024 übertagungen nötig
	 */
	public void test12864() {
		LOGGER.info("START");
		for (int i = 0; i < 1024; i++) {
			lcdConnect.write(i);
		}
		LOGGER.info("ENDE");
	}

	/**
	 * Schreibt cache zu lcd
	 * 
	 */
	public void refreshDisplay() {
		if (lcd12864Cache.isChanged()) {
			LOGGER.info("Write 12864 LCD");
			for (byte b : lcd12864Cache.getBaScreen()) {
				lcdConnect.write(b);
			}
			lcd12864Cache.setChanged(false);
			LOGGER.info("END 12864 LCD");
		}
	}

	/**
	 * 
	 */
	public void setCacheRnd() {
		LOGGER.info("RND 12864");
		byte[] bsRnd = new byte[Lcd12864Cache.SIZE];
		for (int n = 0; n < 1024; n++) {
			bsRnd[n] = (byte) wipf.getRandomInt(255);
		}
		lcd12864Cache.setBaScreen(bsRnd);
	}

	/**
	 * @return
	 * 
	 */
	public Boolean startLCD() {
		LOGGER.info("Starte 12864 LCD");
		return lcdConnect.startPort();

	}

}
