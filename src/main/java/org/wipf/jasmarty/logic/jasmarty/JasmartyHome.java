package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig.lcdType;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.CharPictures;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.CustomChars;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.Lcd2004;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.PageVerwaltung;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class JasmartyHome {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	PageVerwaltung pageVerwaltung;
	@Inject
	ActionVerwaltung actionVerwaltung;
	@Inject
	LcdRefreshLoop lcdRefreshLoop;
	@Inject
	Wipf wipf;
	@Inject
	SerialConfig serialConfig;
	@Inject
	CustomChars customChars;
	@Inject
	CharPictures charPictures;
	@Inject
	Lcd2004 lcd2004;
	@Inject
	Lcd12864 lcd12864;

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

	/**
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		pageVerwaltung.initDB();
		actionVerwaltung.initDB();
		customChars.initDB();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void jasmartyRestart() throws SQLException {
		jasmartyStop();
		jasmartyStart();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void jasmartyStart() throws SQLException {
		LOGGER.info("Starten");
		lcdConnect.setConfig(serialConfig.getConfig());

		if (lcdConnect.getType() == lcdType.LCD_2004) {
			lcd2004.startLCD();
			// lcdConnect.startSerialLcdPort(); wird über start2004LCD

			charPictures.writeAndLoadWipf();
			// charPictures.testChars();

			pageVerwaltung.writeStartPage();
			lcdRefreshLoop.startRefresh2004();
		}

		else if (lcdConnect.getType() == lcdType.LCD_12864) {
			lcd12864.startLCD();
			lcdRefreshLoop.startRefresh12864();
		}

		LOGGER.info("Gestartet");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");
		// TODO für 2004 und 12864

		if (lcdConnect.isLcdOk()) {
			if (lcdConnect.getType() == lcdType.LCD_2004) {
				lcdRefreshLoop.stop();
				wipf.sleep(lcdConnect.getRefreshRate() * 2 + 50);
				pageVerwaltung.writeExitPage();
				lcdRefreshLoop.doRefreshCacheManuell();
				lcd2004.refreshDisplay();
				lcdConnect.closeSerialLcdPort();

			} else if (lcdConnect.getType() == lcdType.LCD_12864) {
				lcdRefreshLoop.stop();
				lcdConnect.closeSerialLcdPort();
			}
		}
		LOGGER.info("Gestoppt");
	}

	/**
	 * 
	 */
	public void doRefreshManuell() {
		// TODO Auto-generated method stub
		LOGGER.info("TODO");
	}

}
