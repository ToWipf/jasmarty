package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
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
		lcd2004.start2004LCD();
		// wird über start2004LCD lcdConnect.startSerialLcdPort();

		charPictures.writeAndLoadWipf();
		// charPictures.testChars();

		pageVerwaltung.writeStartPage();
		lcdRefreshLoop.start();

		LOGGER.info("Gestartet");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");
		if (lcdConnect.isLcdOk()) {
			lcdRefreshLoop.stop();
			wipf.sleep(lcdConnect.getRefreshRate() * 2 + 50);
			pageVerwaltung.writeExitPage();
			lcdRefreshLoop.doRefreshCacheManuell();
			lcd2004.refreshDisplay();
			lcdConnect.closeSerialLcdPort();
		}
		LOGGER.info("Gestoppt");
	}

}
