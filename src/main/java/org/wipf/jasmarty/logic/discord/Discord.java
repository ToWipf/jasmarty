package org.wipf.jasmarty.logic.discord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.Wipf.httpRequestType;
import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Discord {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("Discord");

	/**
	 * @param sId
	 * @return
	 */
	public Integer countOnline(String sId) {
		if (!wipf.isValid(sId)) {
			return -1;
		}
		try {
			String sRes = wipf.httpRequest(httpRequestType.GET,
					"https://discord.com/api/guilds/" + sId + "/widget.json");

			JSONObject jo = new JSONObject(sRes);
			JSONArray ja = (JSONArray) jo.get("members");

			int n = 0;
			for (Object j : ja) {
				JSONObject o = new JSONObject(j.toString());
				if (o.has("channel_id")) {
					n++;
				}
			}

			return n;
		} catch (Exception e) {
			LOGGER.error("Fail: " + e);
			return null;
		}
	}

	/**
	 * @param sDiscordId
	 * @return
	 */
	public Boolean isOnline(String sDiscordId) {
		return (countOnline(sDiscordId) > 0);
	}

	/**
	 * @return
	 */
	public String isOnlineDefault() {
		try {
			String sDid = wipfConfig.getConfParamString("discord_id");
			return sDid + ":" + countOnline(sDid);
		} catch (Exception e) {
			return "Fehler 017" + e;
		}
	}

}
