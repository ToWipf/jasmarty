package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.telegram.extensions.TAppOthers;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

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
	TelegramReadLoop tReadLoop;

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
			LOGGER.info("Testartet");
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
