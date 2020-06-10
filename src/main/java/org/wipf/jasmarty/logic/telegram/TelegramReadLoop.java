package org.wipf.jasmarty.logic.telegram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;

@ApplicationScoped
public class TelegramReadLoop {

	private static final Logger LOGGER = Logger.getLogger("TelegramTask");
	private boolean bLoopActive = false;

	@Inject
	TelegramVerwaltung telegramVerwaltung;
	@Inject
	Wipf wipf;

	/**
	 * 
	 */
	public void start() {
		telegramVerwaltung.setFailCount(1);
		refreshloop();
	}

	/**
	 * 
	 */
	public void stop() {
		if (bLoopActive) {
			bLoopActive = false;
		} else {
			LOGGER.info("Refresh bereits aus");
		}
	}

	/**
	 * 
	 */
	private void refreshloop() {
		if (!bLoopActive) {
			bLoopActive = true;
			LOGGER.info("Refresh an");
		} else {
			LOGGER.info("Refresh bereits an");
			return;
		}

		ExecutorService service = Executors.newFixedThreadPool(1);
		service.submit(new Runnable() {

			@Override
			public void run() {

				while (bLoopActive) {
					telegramVerwaltung.readUpdateFromTelegram();

					// Bei Fehlern warten und später erneut versuchen
					if (telegramVerwaltung.getFailCount() > 5) {
						// Warte 60 sec
						LOGGER.warn("Telegram Task wartet nun 1 min. Fehler Nr. " + telegramVerwaltung.getFailCount());
						wipf.sleep(6000);
					} else if (telegramVerwaltung.getFailCount() > 1) {
						// Warte 20 sec
						LOGGER.warn("Telegram Task wartet nun 40 sec. Fehler Nr. " + telegramVerwaltung.getFailCount());
						wipf.sleep(40000);
					} else {
						// Normales warten
						// TODO aus db holen
						wipf.sleep(20000);
					}

				}

			}
		});
	}
}
