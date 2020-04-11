package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
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
	RefreshLoop refreshLoop;
	@Inject
	Wipf wipf;
	@Inject
	SerialConfig serialConfig;

	private static final Logger LOGGER = Logger.getLogger("app");
	public static final String VERSION = "0.091";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);

		MsqlLite.startDB();
		pageVerwaltung.initDB();
		serialConfig.initDB();

		jaSmartyConnect.setConfig(serialConfig.getConfig());

		if (jaSmartyConnect.startPort()) {
			LOGGER.info("gestartet");

			pageVerwaltung.writeStartPage();
			refreshLoop.start();

		} else {
			LOGGER.info("fail");
		}
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("stoppe ...");
		pageVerwaltung.writeExitPage();
		wipf.sleep(jaSmartyConnect.getRefreshRate() + 50);
		refreshLoop.stop();
		if (jaSmartyConnect.close()) {
			LOGGER.info("gestoppt");
		}
	}

}