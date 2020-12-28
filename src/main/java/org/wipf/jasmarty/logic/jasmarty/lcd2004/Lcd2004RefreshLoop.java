package org.wipf.jasmarty.logic.jasmarty.lcd2004;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.jasmarty.ActionVerwaltung;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Lcd2004RefreshLoop {

	@Inject
	PageConverter pageConverter;
	@Inject
	Lcd2004 lcd2004;
	@Inject
	ActionVerwaltung actionVerwaltung;

	private static final Logger LOGGER = Logger.getLogger("LCD RefreshLoop");
	private boolean bLoopActive = false;

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
	 * @throws Exception
	 * 
	 */
	public void doRefreshCacheManuell() {
		try {
			pageConverter.refreshCache();
		} catch (Exception e) {
			LOGGER.warn("doRefreshCacheManuell fehlgeschlagen. Seite ist fehlerhaft." + e);
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

				while (bLoopActive) {
					try {
						pageConverter.refreshCache();
						if (lcd2004.isLcdOk() && !lcd2004.isbPauseWriteToLCD()) {
							actionVerwaltung.doActionByButtonNr(lcd2004.readButton());
							lcd2004.refreshDisplay();
						}
						Thread.sleep(lcd2004.getRefreshRate());

					} catch (Exception e) {
						LOGGER.warn("Refreshloop fehler: " + e);
						e.printStackTrace();
						break;
					}
				}
				bLoopActive = false;
				LOGGER.info("Refresh aus");
			}
		});
	}

}
