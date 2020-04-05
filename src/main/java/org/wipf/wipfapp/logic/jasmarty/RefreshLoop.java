package org.wipf.wipfapp.logic.jasmarty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ApplicationScoped
public class RefreshLoop {

	@Inject
	PageConverter pageConverter;
	@Inject
	JaSmartyConnect jaSmartyConnect;

	private static final Logger LOGGER = Logger.getLogger("RefreshLoop");

	/**
	 * 
	 */
	public void start() {
		jaSmartyConnect.setLcdOk(true);
		refresh();
	}

	/**
	 * 
	 */
	public void stop() {
		jaSmartyConnect.setLcdOk(false);
	}

	/**
	 * 
	 */
	private void refresh() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.submit(new Runnable() {
			@Override
			public void run() {

				while (jaSmartyConnect.isLcdOk()) {
					try {
						pageConverter.refreshCache();
						jaSmartyConnect.refreshDisplay();

						Thread.sleep(jaSmartyConnect.getRefreshRate());
					} catch (InterruptedException e) {

						LOGGER.warn(e);
						break;
					}
				}
				LOGGER.info("Refresh aus");
			}
		});
	}
}
