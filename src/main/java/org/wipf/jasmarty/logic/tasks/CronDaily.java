package org.wipf.jasmarty.logic.tasks;

import static java.time.DayOfWeek.SUNDAY;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.telegram.TAppGrafana;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;

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
	WipfConfigVW wipfConfig;
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

		if (wipfConfig.isAppActive("telegram")) {

			// Nur Sonntags
			Instant instant = Instant.now();
			ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Berlin"));
			if (zdt.getDayOfWeek() == SUNDAY) {
				tSendAndReceive.sendDaylyInfo();
			}

			appGrafana.deletePictureCache();
			// TODO vorerst nicht mehr senden
			// verwaltung.sendDaylyMotd();

			String sDayLogInfo = daylogHome.getDailyInfo();
			if (!sDayLogInfo.isBlank()) {
				tSendAndReceive.sendMsgToAdmin(sDayLogInfo);
			}
		}
	}

}
