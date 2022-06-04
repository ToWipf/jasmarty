package org.wipf.jasmarty.logic.base.tasks;

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
public class TaskManager {

	@Inject
	TaskDaily taskDaily;

	private static final Logger LOGGER = Logger.getLogger("Daily Task");
	private boolean bTaskRuns = false;

	/**
	 * 
	 */
	// TODO @Scheduled(cron = "0 15 10 * * ?")
	public void startDailyTask() {
		if (!bTaskRuns) {
			LOGGER.info("Starte Daily Task");
			bTaskRuns = true;
			Timer t = new Timer();
			TimerTask taskdaily = taskDaily;
			LocalDateTime localDateTime = LocalDateTime.now();

			long nSekundenBisMitternacht = (86400
					- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));

			// Starte jeden Tag um 00:00 Uhr
			t.scheduleAtFixedRate(taskdaily, nSekundenBisMitternacht * 1000, 86400000);
			// TODO t.cancel(); -> stop task
		} else {
			LOGGER.error("Daily Task ist gerade aktiv!");
		}
	}

}
