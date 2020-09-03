package org.wipf.jasmarty;

/**
 * @author wipf
 *
 */
public class WipfException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	WipfException() {
		super("Wipf Exception ");
	}

	/**
	 * @param s
	 */
	public WipfException(String s) {
		System.out.println(s);
	}
}
