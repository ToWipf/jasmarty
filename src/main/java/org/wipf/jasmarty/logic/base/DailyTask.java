package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
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
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("DailyTask");

	/**
	 *
	 */
	@Override
	public void run() {
		LOGGER.info("Starte DailyTask");
		try {
			if (wipfConfig.isAppActive("telegram")) {
				verwaltung.sendDaylyInfo();
				punkteVW.pluspunkt();
				punkteVW.appendNochSpiel(5);
				// TODO vorerst nicht mehr senden
				// verwaltung.sendDaylyMotd();
			}
		} catch (SQLException e) {
			LOGGER.error("Fail DailyTask");
			e.printStackTrace();
		}
	}

}
