package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.telegram.SendAndReceive;
import org.wipf.jasmarty.logic.wipfapp.PunkteVW;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TaskDaily extends TimerTask {

	@Inject
	SendAndReceive tSendAndReceive;
	@Inject
	PunkteVW punkteVW;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	DaylogHome daylogHome;

	private static final Logger LOGGER = Logger.getLogger("DailyTask");

	/**
	 *
	 */
	@Override
	public void run() {
		LOGGER.info("Starte DailyTask");
		try {
			if (wipfConfig.isAppActive("telegram")) {
				tSendAndReceive.sendDaylyInfo();
				punkteVW.pluspunkt();
				punkteVW.appendNochSpiel(5);
				// TODO vorerst nicht mehr senden
				// verwaltung.sendDaylyMotd();
				tSendAndReceive.sendMsgToAdmin(daylogHome.getTagesinfo()); // TODO zur richtigen ID senden -> id 0
																			// Problem
			}
		} catch (SQLException e) {
			LOGGER.error("Fail DailyTask");
			e.printStackTrace();
		}
	}

}