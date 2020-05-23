package org.wipf.jasmarty.logic.jasmarty;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.QMain;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
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

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

	/**
	 * 
	 */
	public void jasmartyRestart() {
		jasmartyStop();
		jasmartyStart();
	}

	/**
	 * 
	 */
	public void jasmartyStart() {
		LOGGER.info("Starte " + QMain.VERSION);

		pageVerwaltung.initDB();
		serialConfig.initDB();
		actionVerwaltung.initDB();

		lcdConnect.setConfig(serialConfig.getConfig());
		lcdConnect.startSerialLcdPort();

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
