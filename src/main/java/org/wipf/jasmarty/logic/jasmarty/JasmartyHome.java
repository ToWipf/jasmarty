package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864;

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
	Lcd12864 lcd12864;

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

	/**
	 * @throws SQLException
	 */
	private void init() throws SQLException {
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
		init();
		lcdConnect.setConfig(serialConfig.getConfig());

		lcd12864.startLCD();
		lcdRefreshLoop.startRefresh12864();

		LOGGER.info("jasmarty init ende");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");

		if (lcdConnect.isLcdOk()) {
			lcdRefreshLoop.stop();
			lcdConnect.closeSerialLcdPort();
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
