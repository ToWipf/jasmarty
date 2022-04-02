package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.Lcd2004;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class JasmartyHome {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	ActionVerwaltung actionVerwaltung;
	@Inject
	LcdRefreshLoop lcdRefreshLoop;
	@Inject
	Wipf wipf;
	@Inject
	SerialConfig serialConfig;
	@Inject
	Lcd2004 lcd2004;
	@Inject
	Lcd12864 lcd12864;

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

	/**
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		actionVerwaltung.initDB();

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

		switch (lcdConnect.getType()) {
		case LCD_2004:
			if (lcd2004.startLCD()) {
				lcdRefreshLoop.startRefresh2004();
			}
			break;
		case LCD_12864:
			if (lcd12864.startLCD()) {
				lcdRefreshLoop.startRefresh12864();
			}
			break;
		default:
			break;
		}
		LOGGER.info("jasmarty init ende");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");

		if (lcdConnect.isLcdOk()) {
			switch (lcdConnect.getType()) {
			case LCD_2004:
				lcdRefreshLoop.stop();
				wipf.sleep(lcdConnect.getRefreshRate() * 2 + 50);
				lcd2004.stopLCD();
				lcdRefreshLoop.doRefreshCacheManuell();
				lcd2004.refreshDisplay();
				lcdConnect.closeSerialLcdPort();
				break;
			case LCD_12864:
				lcdRefreshLoop.stop();
				lcdConnect.closeSerialLcdPort();
				break;
			default:
				break;
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
