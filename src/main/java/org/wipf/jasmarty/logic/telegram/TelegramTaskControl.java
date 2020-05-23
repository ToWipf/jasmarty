package org.wipf.jasmarty.logic.telegram;

import java.util.Timer;

import org.jboss.logging.Logger;

import com.google.inject.Inject;

public class TelegramTaskControl {

	private static final Logger LOGGER = Logger.getLogger("MStartTelegramTask");

	@Inject
	TelegramVerwaltung telegramVerwaltung;

	/**
	 * 
	 */
	public void startTelegramTask() {
		telegramVerwaltung.setFailCount(1);
		LOGGER.info("Start Telegram Task");
		Timer t = new Timer();
		TelegramTask teleTask = new TelegramTask();

		// TODO
		// LocalDateTime localDateTime = LocalDateTime.now();

		// Integer nSekundenBisMitternacht = (86400
		// - (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 +
		// localDateTime.getSecond()));

		// This task is scheduled to run every 20 seconds
		t.scheduleAtFixedRate(teleTask, 0, 20000);
		// This task is scheduled to run every 1 day at 00:00
		// t.scheduleAtFixedRate(mInfoTask, nSekundenBisMitternacht * 1000, 86400000);
	}
}
