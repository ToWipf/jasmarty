package org.wipf.jasmarty.logic.telegram;

import java.util.TimerTask;

import org.jboss.logging.Logger;

import com.google.inject.Inject;

public class TelegramTask extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger("TaskTelegram");

	@Inject
	TelegramVerwaltung telegramVerwaltung;

	/**
	 *
	 */
	@Override
	public void run() {
		if (telegramVerwaltung.getFailCount() > 6) {
			// Bei viele Fehlern länger warten aber erneut versuchen (2 Minuten fehlerhaft)
			telegramVerwaltung.setFailCountPlusPlus();
			LOGGER.warn("Task Telegram wartet nun " + telegramVerwaltung.getFailCount() + "/12");
			if (telegramVerwaltung.getFailCount() > 12) {
				// 4 Minuten warten
				LOGGER.warn("Task Telegram erneuter Verbindungsversuch");
				telegramVerwaltung.setFailCount(1);
			}
			return;
		}
		try {
			telegramVerwaltung.readUpdateFromTelegram();

		} catch (Exception e) {
			telegramVerwaltung.setFailCountPlusPlus();
			LOGGER.warn("TaskTelegram fails:" + telegramVerwaltung.getFailCount() + " " + e);
		}
	}
}
