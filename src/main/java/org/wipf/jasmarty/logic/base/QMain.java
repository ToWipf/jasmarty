package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.jasmarty.JasmartyHome;
import org.wipf.jasmarty.logic.telegram.TelegramHome;

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
	JasmartyHome jHome;
	@Inject
	TelegramHome tHome;

	private static final Logger LOGGER = Logger.getLogger("QMain");
	public static final String VERSION = "0.65";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);
		MsqlLite.startDB();

		jHome.jasmartyStart();
		tHome.telegramStart();

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

		jHome.jasmartyStop();
		tHome.telegramStop();

		LOGGER.info("Gestoppt");
	}

}