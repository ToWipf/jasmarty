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
public class SendTask {

	@Inject
	InfoTask infoTask;

	private static final Logger LOGGER = Logger.getLogger("TelegramSendTask");
	private boolean bTaskRuns = false;

	/**
	 * 
	 */
	public void startTelegramTask() {
		if (!bTaskRuns) {
			LOGGER.info("Starte Telegram Task");
			bTaskRuns = true;
			Timer t = new Timer();
			TimerTask taskInfo = infoTask;
			LocalDateTime localDateTime = LocalDateTime.now();

			Integer nSekundenBisMitternacht = (86400
					- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));

			// Starte jeden Tag um 00:00 Uhr
			t.scheduleAtFixedRate(taskInfo, nSekundenBisMitternacht * 1000, 86400000);
			// TODO t.cancel(); -> stop task
		} else {
			LOGGER.info("Telegram Task ist bereits aktiv");
		}
	}

}
