package org.wipf.jasmarty.logic.base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.jasmarty.JasmartyHome;
import org.wipf.jasmarty.logic.liste.ListeDB;
import org.wipf.jasmarty.logic.liste.ListeTypeDB;
import org.wipf.jasmarty.logic.telegram.TelegramHome;
import org.wipf.jasmarty.logic.wipfapp.Dynpages;

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
	WipfConfig wipfConfig;
	@Inject
	WipfUserVW wipfUserVW;
	@Inject
	JasmartyHome jasmartyHome;
	@Inject
	TelegramHome telegramHome;
	@Inject
	DaylogHome daylogHome;;
	@Inject
	Dynpages dynpages;
	@Inject
	SqlLitePatcher sqlLitePatcher;
	@Inject
	ListeDB listeDB;
	@Inject
	ListeTypeDB listeTypeDB;

	private static final Logger LOGGER = Logger.getLogger("_MainHome_");
	public static final String VERSION = "1.2.58";
	public static final String DB_PATH = "jasmarty.db";

	/**
	 * Stop App
	 */
	public static void stopApp() {
		LOGGER.info("Stoppen...");
		Quarkus.asyncExit();
	}

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		try {
			LOGGER.info("Starte " + VERSION);
			LOGGER.debug("Debug Log aktiv");
			TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
			// LOGGER.info("Tmp Ordner: " + System.getProperty("java.io.tmpdir"));
			createFileFolder();
			wipfConfig.initDB();

			sqlLitePatcher.doPatch();

			wipfUserVW.initDB();

			if (wipfConfig.isAppActive("wipf")) {
				wipfConfig.checkAppWorkId();
				dynpages.initDB();
				daylogHome.initDB();
			}
			listeDB.initDB();
			listeTypeDB.initDB();

			if (wipfConfig.isAppActive("jasmarty")) {
				jasmartyHome.jasmartyStart();
			}
			if (wipfConfig.isAppActive("telegram")) {
				telegramHome.telegramStart();
			}
			if (wipfConfig.isAppActive("eisenbahn_mitlesen")) {
				System.out.println("eisenbahn_mitlesen aktiv");
			}

		} catch (Exception e) {
			LOGGER.error("Fehler 101: " + e);
			e.printStackTrace();
		}

		LOGGER.info("Gestartet");
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("Stoppe");

		// Alles beenden -> keine db zum fragen vorhanden // TODO evtl jetzt möglich
		jasmartyHome.jasmartyStop();
		telegramHome.telegramStop();

		LOGGER.info("Gestoppt");
	}

	/**
	 * 
	 */
	private void createFileFolder() {
		// Fileorder erstellen
		try {
			Path path = Paths.get(Paths.get("").toAbsolutePath().toString() + "/files");
			Files.createDirectories(path);
		} catch (Exception e) {
			LOGGER.error("createFileFolder");
		}

	}

}