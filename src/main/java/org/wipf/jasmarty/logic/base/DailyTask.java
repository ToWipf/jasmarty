package org.wipf.jasmarty.logic.base;

import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.telegram.SendAndReceive;
import org.wipf.jasmarty.logic.wipfapp.PunkteVW;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class DailyTask extends TimerTask {

	@Inject
	SendAndReceive verwaltung;
	@Inject
	PunkteVW punkteVW;

	/**
	 *
	 */
	@Override
	public void run() {
		verwaltung.sendDaylyInfo();
		punkteVW.pluspunkt();
		punkteVW.appendNochSpiel(5);
		// TODO vorerst nicht mehr senden
		// verwaltung.sendDaylyMotd();
	}

}
