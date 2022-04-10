package org.wipf.jasmarty.logic.discord;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.Wipf.httpRequestType;
import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author devbuntu
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
	 * @throws IOException
	 */
	public Boolean isOnline(String sId) {
		try {
			String sRes = wipf.httpRequest(httpRequestType.GET,
					"https://discord.com/api/guilds/" + sId + "/widget.json");

			JSONObject jo = new JSONObject(sRes);
			JSONArray ja = (JSONArray) jo.get("members");

			for (Object j : ja) {
				JSONObject o = new JSONObject(j.toString());
				if (o.has("channel_id")) {
					return true;
				}
			}

			return false;
		} catch (Exception e) {
			LOGGER.error("Fail: " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	public String isOnlineDefault() {
		try {
			return isOnline(wipfConfig.getConfParamString("discord_id")).toString();
		} catch (Exception e) {
			return "Fehler 017" + e;
		}
	}

}
