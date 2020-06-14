package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramHome {

	// @Inject
	// TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	TelegramVerwaltung tVerwaltung;
	@Inject
	TelegramReadLoop tReadLoop;
	@Inject
	TelegramSendTask tSendTask;

	private static final Logger LOGGER = Logger.getLogger("TelegramHome");

	/**
	 * 
	 */
	public void telegramStart() {
		LOGGER.info("Starten");
		tVerwaltung.initDB();
		appTodoList.initDB();
		appTicTacToe.initDB();

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
