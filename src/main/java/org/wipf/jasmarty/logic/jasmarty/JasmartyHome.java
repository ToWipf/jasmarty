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
	RefreshLoop refreshLoop;
	@Inject
	Wipf wipf;
	@Inject
	SerialConfig serialConfig;
	@Inject
	CustomChars customChars;
	@Inject
	CharPictures charPictures;

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

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
		pageVerwaltung.initDB();
		actionVerwaltung.initDB();
		customChars.initDB();

		lcdConnect.setConfig(serialConfig.getConfig());
		lcdConnect.startSerialLcdPort();

		charPictures.writeAndLoadWipf();
		// charPictures.testChars();

		pageVerwaltung.writeStartPage();
		refreshLoop.start();

		LOGGER.info("Gestartet");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");
		if (lcdConnect.isLcdOk()) {
			refreshLoop.stop();
			wipf.sleep(lcdConnect.getRefreshRate() * 2 + 50);
			pageVerwaltung.writeExitPage();
			refreshLoop.doRefreshCacheManuell();
			lcdConnect.refreshDisplay();
			lcdConnect.closeSerialLcdPort();
		}
		LOGGER.info("Gestoppt");
	}

}
