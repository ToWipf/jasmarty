package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.telegram.extensions.TAppOthers;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

import com.google.inject.Inject;

@ApplicationScoped
public class TelegramHome {

	private static final Logger LOGGER = Logger.getLogger("TelegramHome");

	@Inject
	TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	TelegramVerwaltung tVerwaltung;
	@Inject
	TelegramTaskControl taskControl;

	/**
	 * @param ev
	 */
	void startTelegram() {
		appTicTacToe.initDB();
		tVerwaltung.initDB();
		appTodoList.initDB();

		if (tVerwaltung.loadConfig()) {
			taskControl.startTelegramTask();
			LOGGER.info("Telegram ist gestartet");
		} else {
			LOGGER.warn("Telegram ist nicht gestartet");
		}
	}

	/**
	 * @param ev
	 */
	void stopTelegram() {
		LOGGER.info("stopping...");
		// TODO end Task
	}

}
