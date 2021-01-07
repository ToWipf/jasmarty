package org.wipf.jasmarty.logic.telegram;

import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class InfoTask extends TimerTask {

	@Inject
	SendAndReceive verwaltung;

	/**
	 *
	 */
	@Override
	public void run() {
		verwaltung.sendDaylyInfo();
		// TODO vorerst nicht mehr senden
		// verwaltung.sendDaylyMotd();
	}

}
