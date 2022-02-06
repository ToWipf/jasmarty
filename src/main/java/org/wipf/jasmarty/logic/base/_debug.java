package org.wipf.jasmarty.logic.base;

import java.time.LocalDate;

/**
 * @author Wipf
 *
 */
public class _debug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate dateGestern = LocalDate.now().minusDays(1);

		System.out.println(dateGestern.toString());

		// String sDateNow = df.format(dateGestern);

		// System.out.println(sDateNow);

	}
}
