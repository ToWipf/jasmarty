package org.wipf.jasmarty;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
public class WipfException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	/**
	 * 
	 */
	WipfException() {
		super("Wipf Exception");
	}

	/**
	 * @param s
	 */
	public WipfException(String s) {
		LOGGER.error(s);
	}
}
