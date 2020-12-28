package org.wipf.jasmarty.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ApplicationScoped
public class Lcd12864 {

	private static final Logger LOGGER = Logger.getLogger("Jasmarty 12864");

	@Inject
	LcdConnect lcdConnect;

	/**
	 * 
	 */
	public void test12864() {
		LOGGER.info("START");
		for (int i = 0; i < 1024; i++) {
			lcdConnect.writeAscii(i);
		}
		LOGGER.info("ENDE");
	}

}
