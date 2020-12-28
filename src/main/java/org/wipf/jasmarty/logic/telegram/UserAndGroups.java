package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.telegram.Telegram;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class UserAndGroups {

	/**
	 * TODO ids zu db
	 * 
	 * @param t
	 * @return
	 */
	public Boolean isAdminUser(Telegram t) {
		// Lf: || t.getChatID() == 522467648
		return (t.getChatID() == 798200105 || t.getType().equals("website"));
	}

	/**
	 * TODO to DB
	 * 
	 * @return
	 */
	public int getGroupId() {
		return -387871959;
	}

	/**
	 * @return
	 */
	public int getAdminId() {
		return 798200105;
	}

}
