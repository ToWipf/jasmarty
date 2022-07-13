package org.wipf.jasmarty.logic.eisenbahn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ApplicationScoped
public class Mitlesen {

	@Inject
	MitlesenConnect connect;

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen");
	private boolean bActive = false;

	/**
	 * 
	 */
	public void doStartMitlesen() {
		if (!bActive) {
			LOGGER.info("starten");
			bActive = true;
			ExecutorService service = Executors.newFixedThreadPool(1);
			service.submit(new Runnable() {

				@Override
				public void run() {
					while (bActive) {
						LOGGER.info(connect.readInput());
					}
				}
			});
		} else {
			LOGGER.info("bereits aktiv");
		}
	}

	/**
	 * 
	 */
	public void doStopMitlesen() {
		LOGGER.info("stop");
		bActive = false;

	}

}
