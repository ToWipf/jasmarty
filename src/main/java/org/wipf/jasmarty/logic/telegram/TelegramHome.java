package org.wipf.jasmarty.logic.telegram;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.tasks.RndTask;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramHome {

	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppMsg appTeleMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppRndEvents appRndEvent;
	@Inject
	TAppMedien appMedien;
	@Inject
	TSendAndReceive tVerwaltung;
	@Inject
	TReadLoop tReadLoop;
	@Inject
	TeleLog tLog;
	@Inject
	TUsercache tUsercache;
	@Inject
	RndTask rndTask;
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("TelegramHome");

	/**
	 * @throws SQLException
	 */
	private void init() throws SQLException {
		tLog.initDB();
		appTicTacToe.initDB();
		appTeleMsg.initDB();
		appMotd.initDB();
		appRndEvent.initDB();
		appMedien.initDB();
		tUsercache.initDB();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void telegramStart() throws Exception {
		LOGGER.info("starten");
		init();

		if (tVerwaltung.loadConfig()) {
			tReadLoop.start();

			if (wipfConfig.isAppActive("rndEventTask") && appRndEvent.count() > 1) {
				LOGGER.info("RndEvent Task starten");
				rndTask.startRndTask();
			}

			LOGGER.info("Gestartet");
		} else {
			LOGGER.warn("nicht gestartet");
		}
	}

	/**
	 * 
	 */
	public void telegramStop() {
		LOGGER.info("Stoppen");
		tReadLoop.stop();
		LOGGER.info("Gestoppt");
	}

}
