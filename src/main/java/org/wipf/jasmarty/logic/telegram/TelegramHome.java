package org.wipf.jasmarty.logic.telegram;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.liste.RndEventsService;
import org.wipf.jasmarty.logic.tasks.RndTask;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramHome {

	@Inject
	RndEventsService appRndEvent;
	@Inject
	TReadLoop tReadLoop;
	@Inject
	RndTask rndTask;
	@Inject
	WipfConfigVW wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("TelegramHome");

	/**
	 * @throws SQLException
	 * 
	 */
	public void telegramStart() {
		LOGGER.info("starten");

		tReadLoop.start();

		if (wipfConfig.isAppActive("rndEventTask") && appRndEvent.count() > 1) {
			LOGGER.info("RndEvent Task starten");
			rndTask.startRndTask();
		}

		LOGGER.info("Gestartet");
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
