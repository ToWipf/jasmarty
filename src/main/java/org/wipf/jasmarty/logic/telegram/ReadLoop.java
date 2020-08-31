package org.wipf.jasmarty.logic.telegram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ReadLoop {

	@Inject
	Wipf wipf;
	@Inject
	SendAndReceive telegramVerwaltung;

	private static final Logger LOGGER = Logger.getLogger("TelegramTask");
	private boolean bLoopActive = false;
	private int lastMsgCounter = 4;

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

					switch (telegramVerwaltung.readUpdateFromTelegram()) {
					case 'o':
						// Es gab keine neue Nachrichten
						if (lastMsgCounter == 0) {
							wipf.sleep(60000); // warte 60 sec
						} else {
							// warte nur 20 sec, da gerade geschrieben wurde
							wipf.sleep(15000);
							lastMsgCounter--;
						}
						if (bLastFailed) {
							// Wenn Telegram nicht erreichbar war und nun wieder erreichbar ist. Info
							// senden:
							// Das hat keine Prio -> erst wenn keine neuen Nachrichten ausstehen senden
							telegramVerwaltung.sendExtIp();
							bLastFailed = false;
						}
						break;
					case 'n':
						// Es gab neue Nachrichten -> warte nur 20s
						lastMsgCounter = 6;
						wipf.sleep(20000);
						break;

					case 'f':
						// Es gab einen Fehler
						bLastFailed = true;
						wipf.sleep(60000);
						LOGGER.warn("Telegram hatte einen Fehler -> Warte 1min ");
						break;

					default:
						LOGGER.warn("Telegram unnormales Verhalten");
						bLastFailed = true;
						telegramVerwaltung.sendMsgToAdmin("Abnormales verhalten!");
						wipf.sleep(20000);
						break;
					}

				}

			}
		});
	}
}
