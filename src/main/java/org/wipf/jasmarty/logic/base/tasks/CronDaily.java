package org.wipf.jasmarty.logic.base.tasks;

import java.sql.SQLException;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;
import org.wipf.jasmarty.logic.wipfapp.PunkteVW;

import io.quarkus.scheduler.Scheduled;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class CronDaily  {

	@Inject
	TSendAndReceive tSendAndReceive;
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
	@Scheduled(cron = "0 0 0 1/1 * ? *")
	public void dailyTask() {
		LOGGER.info("Starte DailyTask");
		try {
			if (wipfConfig.isAppActive("telegram")) {
				tSendAndReceive.sendDaylyInfo();
				punkteVW.pluspunkt();
				punkteVW.appendNochSpiel(5);
				// TODO vorerst nicht mehr senden
				// verwaltung.sendDaylyMotd();

				// TODO zur richtigen ID senden -> id 0 Problem
				StringBuilder sb = new StringBuilder();
				sb.append(daylogHome.getTagesInfo());
				sb.append("\n\n");
				sb.append(daylogHome.getGesternInfo());

				tSendAndReceive.sendMsgToAdmin(sb.toString());
			}
		} catch (SQLException e) {
			LOGGER.error("Fail DailyTask");
			e.printStackTrace();
		}
	}

}
