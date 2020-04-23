package org.wipf.jasmarty.logic.jasmarty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class RefreshLoop {

	@Inject
	PageConverter pageConverter;
	@Inject
	LcdConnect lcdConnect;
	@Inject
	ActionVerwaltung actionVerwaltung;

	private static final Logger LOGGER = Logger.getLogger("RefreshLoop");
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

				while (bLoopActive && lcdConnect.isLcdOk()) {
					try {
						actionVerwaltung.doAction(lcdConnect.readButton()); // TODO evtl. in eingen run auslagern

						pageConverter.refreshCache();
						lcdConnect.refreshDisplay();
						Thread.sleep(lcdConnect.getRefreshRate());

					} catch (InterruptedException e) {
						LOGGER.warn(e);
						break;
					}
				}
				bLoopActive = false;
				LOGGER.info("Refresh aus");
			}
		});
	}

}
