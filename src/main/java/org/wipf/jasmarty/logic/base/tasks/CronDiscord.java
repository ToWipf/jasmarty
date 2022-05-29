package org.wipf.jasmarty.logic.base.tasks;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.discord.Discord;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;

import io.quarkus.scheduler.Scheduled;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class CronDiscord {

	@Inject
	Discord discord;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	TSendAndReceive tSendAndReceive;

	private static final Logger LOGGER = Logger.getLogger("DiscordTask");
	private Boolean bLastResult = false;
	private String sDiscordId;

	@PostConstruct
	public void Init() {
		try {
			this.sDiscordId = wipfConfig.getConfParamString("discord_id");
		} catch (SQLException e) {
			// LOGGER.info("Init Discord Fail");
		}
		if (this.sDiscordId == null) {
			// LOGGER.info("Init Discord Fail, id ist null");
		} else {
			LOGGER.info("Init Discord Task: " + sDiscordId);
		}
	}

	/**
	 *
	*/
	@Scheduled(cron = "0 */5 * ? * *")
	public void isOnline() {
		if (sDiscordId != null && sDiscordId != "") {
			Boolean bNow = discord.isOnline(this.sDiscordId);

			// Nur bei wechselnden Status eine Nachricht erstellen
			if (bNow == null && bLastResult != null) {
				tSendAndReceive.sendMsgToAdmin("Fail Discord");
			} else if (bLastResult == null) {
				// nichts senden
			} else if (bNow == true && bLastResult == false) {
				tSendAndReceive.sendMsgToAdmin("Discord Online");
			} else if (bNow == false && bLastResult == true) {
				tSendAndReceive.sendMsgToAdmin("Discord Offline");
			}

			bLastResult = bNow;
		}
	}

}
