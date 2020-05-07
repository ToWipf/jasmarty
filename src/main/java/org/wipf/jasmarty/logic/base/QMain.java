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

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * @author wipf
 *
 */
@QuarkusMain
@ApplicationScoped
public class QMain implements QuarkusApplication {

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

	private static final Logger LOGGER = Logger.getLogger("App");
	public static final String VERSION = "0.55";
	public static final String DB_PATH = "jasmarty.db";
	private static boolean bShutdown = false;

	@Override
	public int run(String... args) throws Exception {
		LOGGER.info("Alles gestartet");
		while (true) {
			Thread.sleep(1000);
			if (bShutdown) {
				LOGGER.info("Beende Programm");
				System.exit(0);
				return 0;
			}
		}
	}

	/**
	 * Stop App
	 */
	public static void stopApp() {
		bShutdown = true;
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

		if (lcdConnect.startPort()) {
			LOGGER.info("Port geöffnet");
		} else {
			LOGGER.warn("Port nicht geöffnet!");
		}
		pageVerwaltung.writeStartPage();
		refreshLoop.start();
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("stoppe ...");
		if (lcdConnect.isLcdOk()) {
			pageVerwaltung.writeExitPage();
			wipf.sleep(lcdConnect.getRefreshRate() + 50);
			refreshLoop.stop();
			if (lcdConnect.close()) {
				LOGGER.info("Port geschlossen");
			}
		}
	}

}