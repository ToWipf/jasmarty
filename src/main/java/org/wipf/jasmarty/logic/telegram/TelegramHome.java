package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppEssen;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppMotd;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppMsg;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppTicTacToe;
import org.wipf.jasmarty.logic.telegram.messageEdit.TAppTodoList;
import org.wipf.jasmarty.logic.telegram.messageEdit.TeleLog;

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
	TAppMsg appTeleMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppEssen appEssen;
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
	 * 
	 */
	public void telegramStart() {
		LOGGER.info("Starten");
		tLog.initDB();
		appTodoList.initDB();
		appTicTacToe.initDB();
		appTeleMsg.initDB();
		appMotd.initDB();
		appEssen.initDB();

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
