package org.wipf.wipfapp.logic.base;

import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.LcdConfig;
import org.wipf.wipfapp.logic.jasmarty.JaSmartyConnect;
import org.wipf.wipfapp.logic.jasmarty.PageVerwaltung;

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

	private static final Logger LOGGER = Logger.getLogger("app");
	public static final String VERSION = "0.041";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);

		MsqlLite.startDB();
		initDBs();

		LcdConfig lconf = new LcdConfig();
		lconf.setPort("COM10");
		lconf.setHight(4);
		lconf.setWidth(20);
		jaSmartyConnect.setConfig(lconf);

		if (jaSmartyConnect.startPort()) {
			LOGGER.info("gestartet");
		} else {
			LOGGER.info("fail");
		}

	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("The application is stopping...");
		if (jaSmartyConnect.close()) {
			LOGGER.info("stopped");
		}
	}

	/**
	 * Tabellen anlegen
	 */
	private void initDBs() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS settings (id, val);");
			pageVerwaltung.initDB();

			pageVerwaltung.test();

		} catch (Exception e) {
			LOGGER.warn("createDBs " + e);
		}
	}
}