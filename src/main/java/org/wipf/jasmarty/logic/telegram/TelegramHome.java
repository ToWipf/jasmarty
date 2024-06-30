package org.wipf.jasmarty.logic.telegram;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.listen.RndEventsService;
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
	 * 
	 * 
	 */
	public void telegramStart() {
		LOGGER.info("starten");

		tReadLoop.start();

		if (wipfConfig.isAppActive("rndEventTask")) {
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
