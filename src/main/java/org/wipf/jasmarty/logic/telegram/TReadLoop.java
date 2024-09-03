package org.wipf.jasmarty.logic.telegram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive.telegramUpdateStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TReadLoop {

	@Inject
	Wipf wipf;
	@Inject
	TSendAndReceive sendAndReceive;

	private static final Logger LOGGER = Logger.getLogger("TelegramTask");
	private boolean bLoopActive = false;
	private int lastMsgCounter = 4;
	private int nFailconter = 0;

	private static final int WARTEZEIT_NACH_SCHREIBEN = 15000; // 15 Sec
	private static final int WARTEZEIT_NORMAL = 60000; // 1 Min

	/**
	 * 
	 */
	public void start() {
		refreshloop();
	}

	/**
	 * 
	 */
	public void stop() {
		if (bLoopActive) {
			bLoopActive = false;
		} else {
			LOGGER.info("Refresh bereits aus");
		}
	}

	/**
	 * 
	 */
	private void refreshloop() {
		if (!bLoopActive) {
			bLoopActive = true;
			LOGGER.info("Refresh an");
			sendAndReceive.sendMsgToAdmin("Starte Telegram Refreshloop");
		} else {
			LOGGER.info("Refresh bereits an");
			return;
		}

		ExecutorService service = Executors.newFixedThreadPool(1);
		service.submit(new Runnable() {

			@Override
			public void run() {

				boolean bLastFailed = true;
				while (bLoopActive) {

					switch (sendAndReceive.readUpdateFromTelegram()) {
					case telegramUpdateStatus.FERTIG_NICHTS:
						// Es gab keine neue Nachrichten
						if (lastMsgCounter == 0) {
							wipf.sleep(WARTEZEIT_NORMAL); // warte 60 sec
						} else {
							// warte nur 15 sec, da gerade geschrieben wurde
							wipf.sleep(WARTEZEIT_NACH_SCHREIBEN);
							lastMsgCounter--;
							// Failcounter zurücksetzen
							nFailconter = 0;
						}
						if (bLastFailed) {
							// Wenn Telegram nicht erreichbar war und nun wieder erreichbar ist.
							// Info senden:
							// Das hat keine Prio -> erst wenn keine neuen Nachrichten ausstehen senden
							sendAndReceive.sendMsgToAdmin("Telegram online nach: " + nFailconter + ". IP: " + wipf.getExternalIp());

							bLastFailed = false;
							LOGGER.info("Telegram ist erreichbar");
						}
						break;

					case telegramUpdateStatus.FERTIG_NEUE:
						// Es gab neue Nachrichten -> warte nur 20s
						lastMsgCounter = 6;
						wipf.sleep(WARTEZEIT_NACH_SCHREIBEN);
						break;

					case telegramUpdateStatus.FEHLER_ALARM:
						// Es gab einen Fehler
						bLastFailed = true;
						wipf.sleep(WARTEZEIT_NORMAL);
						LOGGER.warn("Telegram hatte einen Fehler -> Warte 1min ");
						nFailconter++;
						break;

					default:
						LOGGER.warn("Telegram unnormales Verhalten, welches ignoriert wird");
						// bLastFailed = true;
						wipf.sleep(WARTEZEIT_NORMAL); // 1 Minute warten
						break;
					}
				}

			}
		});
	}
}
