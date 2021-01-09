package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;
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
	 * Cache senden an lcd
	 * 
	 */
	public void refreshDisplay() {
		if (lcd12864Cache.isChanged()) {
			for (byte b : lcd12864Cache.getPage().getScreenAsByteArray()) {
				lcdConnect.write(b);
			}
			lcd12864Cache.setChanged(false);
		}
	}

	/**
	 * 
	 */
	public void setCacheRnd() {
		LOGGER.info("RND 12864");
		byte[] bsRnd = new byte[Lcd12864Page.SIZE];
		for (int n = 0; n < 1024; n++) {
			bsRnd[n] = (byte) wipf.getRandomInt(255);
		}
		lcd12864Cache.setScreen(new Lcd12864Page(bsRnd));
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
