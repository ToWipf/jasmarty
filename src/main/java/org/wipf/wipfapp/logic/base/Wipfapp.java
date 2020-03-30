package org.wipf.wipfapp.logic.base;

import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.logic.telegram.apps.MEssen;
import org.wipf.wipfapp.logic.telegram.apps.MMumel;
import org.wipf.wipfapp.logic.telegram.apps.MTicTacToe;
import org.wipf.wipfapp.logic.telegram.apps.MTodoList;
import org.wipf.wipfapp.logic.telegram.system.MTeleMsg;
import org.wipf.wipfapp.logic.telegram.system.MTelegram;
import org.wipf.wipfapp.logic.telegram.task.MStartTelegramTask;

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
public class Wipfapp {

	private static final Logger LOGGER = Logger.getLogger("wipfapp");
	public static final String VERSION = "2.84";
	public static final String DB_PATH = System.getProperty("user.home") + "/wipfapp/" + "wipfapp.db";
	public static final String ELCD_PATH = "http://192.168.2.242/";
	public static final String sKey = "superKey42";

	public static Integer FailCountElcd;
	public static Integer FailCountTelegram;
	public static Boolean RunLock = false;
	public static Integer TelegramOffsetID;
	public static String BOTKEY;

	/**
	 * @param ev
	 */
	void onStart(@Observes StartupEvent ev) {
		System.out.println("_________________________");
		LOGGER.info("Starte WipfApp " + VERSION);

		MsqlLite.startDB();
		initDBs();
		if (MTelegram.loadConfig()) {
			MStartTelegramTask.startTelegramTask();
		}
		System.gc();
		LOGGER.info("Wipfapp ist gestartet");
	}

	/**
	 * @param ev
	 */
	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("The application is stopping...");
		// System.exit(0);
		// TODO funktioniert nicht
		// https://github.com/quarkusio/quarkus/issues/2150
	}

	/**
	 * Tabellen anlegen
	 */
	private static void initDBs() {
		MTicTacToe.initDB();
		MTelegram.initDB();
		MTeleMsg.initDB();
		MMumel.initDB();
		MEssen.initDB();
		MTodoList.initDB();

		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS settings (id, val);");

		} catch (Exception e) {
			LOGGER.warn("createDBs " + e);
		}
	}
}