package org.wipf.jasmarty.logic.tasks;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Discord;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.discord.DiscordHome;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;

import io.quarkus.scheduler.Scheduled;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class CronDiscord {

	@Inject
	DiscordHome discord;
	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	TSendAndReceive tSendAndReceive;

	private static final Logger LOGGER = Logger.getLogger("DiscordTask");
	private Discord dLast = new Discord();
	private String sDiscordId = null;

	@PostConstruct
	public void Init() {
		if (wipfConfig.isAppActive("telegram")) {

			this.sDiscordId = wipfConfig.getConfParamString("discord_id");

			if (this.sDiscordId == null) {
				// LOGGER.info("Init Discord Fail, id ist null");
			} else {
				LOGGER.info("Init Discord Task: " + sDiscordId);
			}
		}
	}

	/**
	 *
	*/
	@Scheduled(cron = "0 */5 * ? * *") // Jede 5. Minute
	public void isOnline() {
		if (sDiscordId != null && sDiscordId != "") {
			Discord dNow = discord.getById(this.sDiscordId);

			// Nur bei wechselnden is Valid Status eine Nachricht erstellen
			if (!dNow.isValid() && dLast.isValid()) {
				tSendAndReceive.sendMsgToAdmin("Fail Discord");
			} else if (dNow.countUser() > dLast.countUser()) {
				tSendAndReceive.sendMsgToAdmin("Discord Online\n\n" + dNow.userToString());
			} else if (dNow.countUser() > 0 && dNow.countUser() < dLast.countUser()) {
				tSendAndReceive.sendMsgToAdmin("Discord weniger Online\n\n" + dNow.userToString());
			} else if (dNow.countUser() == 0 && dNow.countUser() < dLast.countUser()) {
				tSendAndReceive.sendMsgToAdmin("Discord Offline");
			} else if (dNow.isValid() && !dLast.isValid()) {
				tSendAndReceive.sendMsgToAdmin("Discord ok");
			}

			dLast = dNow;
		}
	}

}
