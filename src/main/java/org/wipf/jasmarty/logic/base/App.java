package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.jasmarty.ActionVerwaltung;
import org.wipf.jasmarty.logic.jasmarty.JaSmartyConnect;
import org.wipf.jasmarty.logic.jasmarty.PageVerwaltung;
import org.wipf.jasmarty.logic.jasmarty.RefreshLoop;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class App {

	@Inject
	JaSmartyConnect jaSmartyConnect;
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

	private static final Logger LOGGER = Logger.getLogger("app");
	public static final String VERSION = "0.108";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);

		MsqlLite.startDB();
		pageVerwaltung.initDB();
		serialConfig.initDB();
		actionVerwaltung.initDB();

		jaSmartyConnect.setConfig(serialConfig.getConfig());

		if (jaSmartyConnect.startPort()) {
			LOGGER.info("Port geöffnet");

			pageVerwaltung.writeStartPage();
			refreshLoop.start();

		} else {
			LOGGER.warn("Port nicht geöffnet!");
		}
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("stoppe ...");
		if (jaSmartyConnect.isLcdOk()) {
			pageVerwaltung.writeExitPage();
			wipf.sleep(jaSmartyConnect.getRefreshRate() + 50);
			refreshLoop.stop();
			if (jaSmartyConnect.close()) {
				LOGGER.info("Port geschlossen");
			}
		}
	}

}