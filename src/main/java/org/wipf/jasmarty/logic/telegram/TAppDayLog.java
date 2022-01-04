package org.wipf.jasmarty.logic.telegram;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.telegram.Telegram;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class TAppDayLog {

	@Inject
	TLastMessageFromUser tLastMessageFromUser;

	public String telegramMenue(Telegram t) {
		String sLastMsg = tLastMessageFromUser.getTheLastMessage(t);

		return "LAST MSG: " + sLastMsg;
	}

}
