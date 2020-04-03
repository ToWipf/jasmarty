package org.wipf.wipfapp.logic.base;

import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.logic.jasmarty.JaSmartyConnect;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

// TODO:
/*
 * //@formatter:off
 * stringclass
 * alle confs in db
 * 4 gewinnt
 * add to motd for id
 * set a new admin ?
 * sende in Stunden nachricht
 * rechner tage in stunden
 * zeitgeplante nachrichten z.B send 10m Hallo Test
 * motd für bestimmte Tage
 * admin tabelle (Telegram ids nicht in code)
 * sammelen aller user in tabelle mit rechten
 * //@formatter:on
 */

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class App {

	@Inject
	JaSmartyConnect jaSmartyConnect;

	private static final Logger LOGGER = Logger.getLogger("jasmarty");
	public static final String VERSION = "0.21";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		System.out.println("_________________________");
		LOGGER.info("Starte " + VERSION);

		MsqlLite.startDB();
		initDBs();
		if (jaSmartyConnect.open()) {
			LOGGER.info("gestartet");
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
	private static void initDBs() {

		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS settings (id, val);");

		} catch (Exception e) {
			LOGGER.warn("createDBs " + e);
		}
	}
}