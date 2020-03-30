package org.wipf.wipfapp.logic.telegram.task;

import java.time.LocalDateTime;
import java.util.Timer;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.logic.base.Wipfapp;

public class MStartTelegramTask {

	private static final Logger LOGGER = Logger.getLogger("MStartTelegramTask");

	/**
	 * 
	 */
	public static void startTelegramTask() {
		Wipfapp.FailCountTelegram = 1;
		LOGGER.info("Start Telegram Task");
		Timer t = new Timer();
		TaskTelegram mTask = new TaskTelegram();
		TaskInfoTelegram mInfoTask = new TaskInfoTelegram();

		LocalDateTime localDateTime = LocalDateTime.now();

		Integer nSekundenBisMitternacht = (86400
				- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));

		// This task is scheduled to run every 20 seconds
		t.scheduleAtFixedRate(mTask, 0, 20000);
		// This task is scheduled to run every 1 day at 00:00
		t.scheduleAtFixedRate(mInfoTask, nSekundenBisMitternacht * 1000, 86400000);
	}
}
