package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;

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
	Lcd2004RefreshLoop lcd2004refreshLoop;
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
		lcd2004refreshLoop.start();

		LOGGER.info("Gestartet");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");
		if (lcdConnect.isLcdOk()) {
			lcd2004refreshLoop.stop();
			wipf.sleep(lcdConnect.getRefreshRate() * 2 + 50);
			pageVerwaltung.writeExitPage();
			lcd2004refreshLoop.doRefreshCacheManuell();
			lcd2004.refreshDisplay();
			lcdConnect.closeSerialLcdPort();
		}
		LOGGER.info("Gestoppt");
	}

}
