package org.wipf.jasmarty.logic.discord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.Discord;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.Wipf.httpRequestType;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class DiscordHome {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("Discord");

	/**
	 * @param sId
	 * @return
	 */
	public Discord getById(String sId) {
		Discord d = new Discord();
		if (!wipf.isValid(sId)) {
			return d;
		}
		try {
			d.setDisId(sId);
			String sRes = wipf.httpRequest(httpRequestType.GET, "https://discord.com/api/guilds/" + sId + "/widget.json");

			JSONObject jo = new JSONObject(sRes);
			JSONArray ja = (JSONArray) jo.get("members");

			for (Object j : ja) {
				JSONObject o = new JSONObject(j.toString());
				if (o.has("channel_id")) {
					d.addUser(o.getString("username"));
				}
			}
			d.setValid(true);

		} catch (Exception e) {
			LOGGER.error("Fail: " + e);
			d.setValid(false);
		}
		return d;
	}

	/**
	 * @return
	 */
	public Discord getByDefaultId() {
		return getById(wipfConfig.getConfParamString("discord_id"));
	}

	/**
	 * @return
	 */
	public String getUserAsString() {
		Discord d = getByDefaultId();
		if (d == null) {
			return "Fehler 119";
		} else {
			return d.userToString();
		}
	}

}
