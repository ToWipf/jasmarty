package org.wipf.jasmarty.logic.telegram;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramHome {

	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	TAppMsg appTeleMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppEssen appEssen;
	@Inject
	TAppFilm appFilme;
	@Inject
	SendAndReceive tVerwaltung;
	@Inject
	ReadLoop tReadLoop;
	@Inject
	SendTask tSendTask;
	@Inject
	TeleLog tLog;

	private static final Logger LOGGER = Logger.getLogger("TelegramHome");

	/**
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		tLog.initDB();
		appTodoList.initDB();
		appTicTacToe.initDB();
		appTeleMsg.initDB();
		appMotd.initDB();
		appEssen.initDB();
		appFilme.initDB();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void telegramStart() throws SQLException {
		LOGGER.info("starten");

		if (tVerwaltung.loadConfig()) {
			tReadLoop.start();
			tSendTask.startTelegramTask();
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
