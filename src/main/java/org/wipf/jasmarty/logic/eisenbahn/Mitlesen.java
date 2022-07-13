package org.wipf.jasmarty.logic.eisenbahn;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ApplicationScoped
public class Mitlesen {

	@Inject
	MitlesenConnect connect;

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen");

	/**
	 * 
	 */
	public void doMitlesen() {
		while (true) {
			LOGGER.info(connect.readInput());
		}

	}

}
