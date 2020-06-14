package org.wipf.jasmarty.logic.telegram;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramSendTask {

	@Inject
	TelegramInfoTask infoTask;

	private static final Logger LOGGER = Logger.getLogger("TelegramSendTask");

	/**
	 * 
	 */
	public void startTelegramTask() {
		LOGGER.info("Starte Telegram Task");
		Timer t = new Timer();
		TimerTask taskInfo = infoTask;
		LocalDateTime localDateTime = LocalDateTime.now();

		Integer nSekundenBisMitternacht = (86400
				- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));

		// Starte jeden Tag um 00:00 Uhr
		t.scheduleAtFixedRate(taskInfo, nSekundenBisMitternacht * 1000, 86400000);
	}

}
