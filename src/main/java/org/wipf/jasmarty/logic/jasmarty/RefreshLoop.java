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
	JaSmartyConnect jaSmartyConnect;

	private static final Logger LOGGER = Logger.getLogger("RefreshLoop");
	private boolean bLoopActive = false;

	/**
	 * 
	 */
	public void start() {
		jaSmartyConnect.resetLcdOK();
		refreshloop();
	}

	/**
	 * 
	 */
	public void stop() {
		bLoopActive = false;
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

				while (bLoopActive && jaSmartyConnect.isLcdOk()) {
					try {
						pageConverter.refreshCache();
						jaSmartyConnect.refreshDisplay();
						Thread.sleep(jaSmartyConnect.getRefreshRate());

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
