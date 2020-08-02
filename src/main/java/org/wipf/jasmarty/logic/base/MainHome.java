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
public class MainHome {

	@Inject
	BaseSettings baseSettings;
	@Inject
	Auth auth;
	@Inject
	JasmartyHome jHome;
	@Inject
	TelegramHome tHome;

	private static final Logger LOGGER = Logger.getLogger("_MainHome_");
	public static final String VERSION = "0.80";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * Stop App
	 */
	public static void stopApp() {
		Quarkus.asyncExit();
	}

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		LOGGER.info("Starte " + VERSION);
		LOGGER.info("Tmp Ordner: " + System.getProperty("java.io.tmpdir"));

		SqlLite.startDB();

		baseSettings.initDB();
		auth.initDB();

		if (baseSettings.isAppActive("jasmarty")) {
			jHome.jasmartyStart();
		}
		if (baseSettings.isAppActive("telegram")) {
			tHome.telegramStart();
		}

		LOGGER.info("Gestartet");
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("Stoppe");

		// Alles beenden -> keine db zum fragen vorhanden
		jHome.jasmartyStop();
		tHome.telegramStop();

		LOGGER.info("Gestoppt");
	}

}