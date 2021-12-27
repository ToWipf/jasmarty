package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.daylog.DaylogEvent;
import org.wipf.jasmarty.logic.jasmarty.JasmartyHome;
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
	JasmartyHome jHome;
	@Inject
	TelegramHome tHome;
	@Inject
	TaskManager taskmanager;
	@Inject
	DaylogEvent daylogEvent;
	@Inject
	Dynpages dynpages;
	@Inject
	SqlLitePatcher sqlLitePatcher;

	private static final Logger LOGGER = Logger.getLogger("_MainHome_");
	public static final String VERSION = "1.0.46";
	public static final Integer DB_PATCH_VERSION = 1;
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
		try {
			LOGGER.info("Starte " + VERSION);
			// LOGGER.info("Tmp Ordner: " + System.getProperty("java.io.tmpdir"));
			wipfConfig.initDB();

			sqlLitePatcher.doPatch();

			wipfUserVW.initDB();
			dynpages.initDB();
			daylogEvent.initDB();
			wipfConfig.checkAppWorkId();
			taskmanager.startDailyTask();

			jHome.init();
			tHome.init();

			if (wipfConfig.isAppActive("jasmarty")) {
				jHome.jasmartyStart();
			}
			if (wipfConfig.isAppActive("telegram")) {
				tHome.telegramStart();
			}

		} catch (SQLException e) {
			LOGGER.info("SQLException");
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
		jHome.jasmartyStop();
		tHome.telegramStop();

		LOGGER.info("Gestoppt");
	}

}