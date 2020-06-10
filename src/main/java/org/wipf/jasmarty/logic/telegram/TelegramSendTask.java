package org.wipf.jasmarty.logic.telegram;

import java.time.LocalDateTime;
import java.util.Timer;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramSendTask {

	private static final Logger LOGGER = Logger.getLogger("TelegramSendTask");

	/**
	 * 
	 */
	public void startTelegramTask() {
		LOGGER.info("Starte Telegram Task");
		Timer t = new Timer();
		TelegramInfoTask taskInfo = new TelegramInfoTask();
		LocalDateTime localDateTime = LocalDateTime.now();

		Integer nSekundenBisMitternacht = (86400
				- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));

		// This task is scheduled to run every day at 00:00
		t.scheduleAtFixedRate(taskInfo, nSekundenBisMitternacht * 1000, 86400000);
	}

}
