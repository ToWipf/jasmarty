package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font.Lcd12864fontType;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page.lineAlignment;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase;
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
		byte[] bsRnd = new byte[1024];
		for (int n = 0; n < 1024; n++) {
			bsRnd[n] = (byte) wipf.getRandomInt(255);
		}
		lcd12864Cache.setScreen(new Lcd12864PageBase(bsRnd));
	}

	/**
	 * 
	 */
	public void testDisplayText() {
		Lcd12864Page lp = new Lcd12864Page();
		Lcd12864Font font57 = new Lcd12864Font(Lcd12864fontType.FONT_57);
		Lcd12864Font font68 = new Lcd12864Font(Lcd12864fontType.FONT_68);
		lp.drawString(font57, 20, 10, lineAlignment.CUSTOM, "Hallo Welt!");
		lp.drawString(font57, null, 30, lineAlignment.LEFT, "\"ABC123!456\"");
		lp.drawString(font68, null, 55, lineAlignment.CENTER, "Jasmarty F");
		lp.drawString(font57, null, 0, lineAlignment.LEFT, "Wipf");

		lcd12864Cache.setScreen(lp);
	}

	/**
	 * 
	 */
	public void testDisplayFont57() {
		Lcd12864Page lp = new Lcd12864Page();
		Lcd12864Font font57 = new Lcd12864Font(Lcd12864fontType.FONT_57);
		lp.drawString(font57, 0, 0, lineAlignment.CUSTOM,
				" !\"#$%& '()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
		lcd12864Cache.setScreen(lp);
	}

	/**
	 * 
	 */
	public void testDisplayFont68() {
		Lcd12864Page lp = new Lcd12864Page();
		Lcd12864Font font = new Lcd12864Font(Lcd12864fontType.FONT_68);
		lp.drawString(font, 0, 0, lineAlignment.CUSTOM,
				" !\"#$%& '()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
		lcd12864Cache.setScreen(lp);
	}

	/**
	 * 
	 */
	public void testDisplayFunctions() {
		System.out.println("test1");
		for (int i = 0; i <= 128; i++) {
			Lcd12864Page lp = new Lcd12864Page();
			lp.drawCircleFill(64, 32, i / 3);
			lp.drawLineH(0, 128, i / 2);
			lcd12864Cache.setScreen(lp);
			wipf.sleep(220);
			System.out.println(i);
		}
		System.out.println("test2");
		for (int i = 0; i <= 128; i++) {
			Lcd12864Page lp = new Lcd12864Page();
			lp.drawRectFill(0, 0, i, i / 2);
			lp.drawLineV(i, 0, 64);
			lcd12864Cache.setScreen(lp);
			wipf.sleep(220);
			System.out.println(i);
		}
		System.out.println("test3");
		for (int i = 0; i <= 128; i++) {
			Lcd12864Page lp = new Lcd12864Page();
			lp.drawLine(64, 32, i, i / 2);
			lp.drawCircle(64, 32, i / 3);
			lp.drawRect(0, 0, i, i / 2);
			lcd12864Cache.setScreen(lp);
			wipf.sleep(220);
			System.out.println(i);
		}
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
