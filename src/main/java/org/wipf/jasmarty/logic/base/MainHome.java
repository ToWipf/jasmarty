package org.wipf.jasmarty.logic.base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.lcd.JasmartyHome;
import org.wipf.jasmarty.logic.listen.ListeService;
import org.wipf.jasmarty.logic.listen.ListeTypeService;
import org.wipf.jasmarty.logic.telegram.TelegramHome;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * @author wipf
 *
 */
@RequestScoped
public class MainHome {

	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	WipfUserVW wipfUserVW;
	@Inject
	JasmartyHome jasmartyHome;
	@Inject
	TelegramHome telegramHome;
	@Inject
	ListeService listeDB;
	@Inject
	ListeTypeService listeTypeDB;
	@Inject
	AuthKeyService authKeyService;

	private static final Logger LOGGER = Logger.getLogger("_MainHome_");
	public static final String VERSION = "2.2.16";
	public static final String AUTH_KEY_NAME = "authKey";

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
		LOGGER.info("Starte " + VERSION);
		LOGGER.debug("Debug Log aktiv");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		// LOGGER.info("Tmp Ordner: " + System.getProperty("java.io.tmpdir"));
		createFileFolder();
		authKeyService.loadKeysIntoCache();

		wipfUserVW.crateDefaultUsers();

		if (wipfConfig.isAppActive("wipf")) {
			wipfConfig.checkAppWorkId();
		}

		if (wipfConfig.isAppActive("jasmarty")) {
			jasmartyHome.jasmartyStart();
		}
		if (wipfConfig.isAppActive("telegram")) {
			telegramHome.telegramStart();
		}
		if (wipfConfig.isAppActive("eisenbahn_mitlesen")) {
			System.out.println("eisenbahn_mitlesen aktiv");
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