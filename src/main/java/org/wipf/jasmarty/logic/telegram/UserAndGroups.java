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
	 * @return
	 */
	public int getAdminId() {
		return 798200105;
	}

	/**
	 * @return
	 */
	public boolean isUser(Telegram t) {
		return (isAdminUser(t) || t.getChatID() == 522467648 || t.getChatID() == 1750235711
				|| t.getChatID() == -584490153);
	}

	/**
	 * @param ChatId
	 * @return
	 */
	public boolean isUser(Integer ChatId) {
		Telegram t = new Telegram();
		t.setChatID(ChatId);
		return isUser(t);
	}

}
