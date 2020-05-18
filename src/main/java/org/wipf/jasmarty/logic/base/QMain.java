package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.jasmarty.ActionVerwaltung;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;
import org.wipf.jasmarty.logic.jasmarty.PageVerwaltung;
import org.wipf.jasmarty.logic.jasmarty.RefreshLoop;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class QMain {

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

	private static final Logger LOGGER = Logger.getLogger("QMain");
	public static final String VERSION = "0.60";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * 
	 */
	public void startAgain() {
		onStart(null);
	}

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);

		MsqlLite.startDB();
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
	 * Stop App
	 */
	public static void stopApp() {
		Quarkus.asyncExit();
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
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
		wipf.printLogo();
	}

}