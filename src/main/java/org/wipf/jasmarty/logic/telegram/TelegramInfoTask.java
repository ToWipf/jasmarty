package org.wipf.jasmarty.logic.telegram;

import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TelegramInfoTask extends TimerTask {

	@Inject
	TelegramVerwaltung verwaltung;

	/**
	 *
	 */
	@Override
	public void run() {
		verwaltung.sendDaylyInfo();
		verwaltung.sendDaylyMotd();
	}
}
