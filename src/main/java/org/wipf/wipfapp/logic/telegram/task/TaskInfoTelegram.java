package org.wipf.wipfapp.logic.telegram.task;

import java.util.TimerTask;

import org.wipf.wipfapp.logic.telegram.system.MTeleMsg;

/**
 * @author wipf
 *
 */
public class TaskInfoTelegram extends TimerTask {

	/**
	 *
	 */
	@Override
	public void run() {
		// Senden
		MTeleMsg.sendDaylyInfo();
		MTeleMsg.sendDaylyMotd();
		// MEssen.sendDaylyEssen();
	}
}
