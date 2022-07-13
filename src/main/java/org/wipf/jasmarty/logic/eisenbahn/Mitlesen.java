package org.wipf.jasmarty.logic.eisenbahn;

import java.util.LinkedHashSet;
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
	private LinkedHashSet<String> items;

	/**
	 * 
	 */
	public void doStartMitlesen() {
		if (!bActive) {
			LOGGER.info("starten");
			items = new LinkedHashSet<String>();
			bActive = true;
			ExecutorService service = Executors.newFixedThreadPool(1);
			service.submit(new Runnable() {

				@Override
				public void run() {
					while (bActive) {
						char[] nIn = connect.readInput();
						if (nIn != null) {
							StringBuilder sb = new StringBuilder();
							for (char c : nIn) {
								sb.append(c);
							}
							LOGGER.info(sb.toString());

						}
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

//	pirvate void addItem() {

//	}

}
