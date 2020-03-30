package org.wipf.wipfapp.logic.telegram.task;

import java.util.TimerTask;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.logic.base.Wipfapp;
import org.wipf.wipfapp.logic.telegram.system.MTelegram;

/**
 * @author wipf
 *
 */
public class TaskTelegram extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger("TaskTelegram");

	/**
	 *
	 */
	@Override
	public void run() {
		if (Wipfapp.FailCountTelegram > 6) {
			// Bei viele Fehlern länger warten aber erneut versuchen (2 Minuten fehlerhaft)
			Wipfapp.FailCountTelegram++;
			LOGGER.warn("Task Telegram wartet nun " + Wipfapp.FailCountTelegram + "/12");
			if (Wipfapp.FailCountTelegram > 12) {
				// 4 Minuten warten
				LOGGER.warn("Task Telegram erneuter Verbindungsversuch");
				Wipfapp.FailCountTelegram = 1;
			}
			return;
		}
		try {
			MTelegram.readUpdateFromTelegram();

		} catch (Exception e) {
			Wipfapp.FailCountTelegram++;
			LOGGER.warn("TaskTelegram fails:" + Wipfapp.FailCountTelegram + " " + e);
		}
	}
}
