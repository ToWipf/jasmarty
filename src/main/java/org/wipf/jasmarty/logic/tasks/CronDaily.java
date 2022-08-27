package org.wipf.jasmarty.logic.tasks;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.telegram.TAppGrafana;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;
import org.wipf.jasmarty.logic.wipfapp.PunkteVW;

import io.quarkus.scheduler.Scheduled;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class CronDaily {

	@Inject
	TSendAndReceive tSendAndReceive;
	@Inject
	PunkteVW punkteVW;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	DaylogHome daylogHome;
	@Inject
	TAppGrafana appGrafana;

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
				appGrafana.deletePictureCache();
				// TODO vorerst nicht mehr senden
				// verwaltung.sendDaylyMotd();

				String sDayLogInfo = daylogHome.getDailyInfo();
				if (!sDayLogInfo.isBlank()) {
					tSendAndReceive.sendMsgToAdmin(sDayLogInfo);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Fail DailyTask");
			e.printStackTrace();
		}
	}

}
