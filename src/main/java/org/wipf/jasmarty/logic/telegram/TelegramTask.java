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
		if (telegramVerwaltung.getFailCount() > 1) {
			// Bei viele Fehlern länger warten und später erneut versuchen
			telegramVerwaltung.setFailCountPlusPlus();
			LOGGER.warn("Telegram Task wartet nun " + telegramVerwaltung.getFailCount() + "/12");
			if (telegramVerwaltung.getFailCount() > 12) {
				LOGGER.warn("Telegram Task probiert erneuten Verbindungsversuch");
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
