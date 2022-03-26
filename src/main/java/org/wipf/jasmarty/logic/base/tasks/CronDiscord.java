package org.wipf.jasmarty.logic.base.tasks;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.discord.Discord;
import org.wipf.jasmarty.logic.telegram.SendAndReceive;

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
	SendAndReceive tSendAndReceive;

	private static final Logger LOGGER = Logger.getLogger("DiscordTask");
	private Boolean bLastResult = false;
	private String sDiscordId = "";

	@PostConstruct
	public void Init() {
		LOGGER.info("Init Discord Task");
		this.sDiscordId = "";
	}

	/**
	 *
	 */
	@Scheduled(cron = "0 */5 * ? * *")
	public void isOnline() {
		System.out.println("NOW");
		Boolean bNow = discord.isOnline(this.sDiscordId);

		// Nur bei wechselnden Status eine Nachricht erstellen
		if (bNow == null && bLastResult != null) {
			LOGGER.error("Fail Discord");
			tSendAndReceive.sendMsgToAdmin("Fail Discord");
		}
		if (bNow == true && bLastResult == false) {
			LOGGER.info("Discord Online");
			tSendAndReceive.sendMsgToAdmin("Discord Online");
		}
		if (bNow == false && bLastResult == true) {
			LOGGER.info("Discord Offline");
			tSendAndReceive.sendMsgToAdmin("Discord Offline");
		}

		bLastResult = bNow;
	}

}
