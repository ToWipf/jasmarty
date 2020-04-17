package org.wipf.jasmarty.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Actions {

	/**
	 * @param nID
	 */
	public void doAction(Integer nID) {
		if (nID != null) {
			System.out.println("Action: " + nID);
		}
	}
}
