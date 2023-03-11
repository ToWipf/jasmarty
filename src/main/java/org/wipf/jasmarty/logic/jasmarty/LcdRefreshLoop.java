package org.wipf.jasmarty.logic.jasmarty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864PageConverter;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class LcdRefreshLoop {

	@Inject
	Lcd12864 lcd12864;
	@Inject
	Lcd12864PageConverter lcd12864PageConverter;
	@Inject
	ActionVerwaltung actionVerwaltung;
	@Inject
	LcdConnect lcdConnect;

	private static final Logger LOGGER = Logger.getLogger("LCD RefreshLoop");
	private boolean bLoopActive = false;

	/**
	 * 
	 */
	public void startRefresh12864() {
		refreshloop12864();
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
	private void refreshloop12864() {
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
						if (lcdConnect.isLcdOk()) {
							actionVerwaltung.doActionByButtonNr(lcdConnect.readButton());
							lcd12864PageConverter.refreshCurrentPageToCache();
							lcd12864.refreshDisplay();
						}
						Thread.sleep(lcdConnect.getRefreshRate() + lcd12864PageConverter.getCurrentTimeoutime());

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
