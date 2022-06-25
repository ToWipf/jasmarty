package org.wipf.jasmarty.logic.discord;

import java.util.LinkedList;
import java.util.List;

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
			return -2;
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
			return "Fehler 110" + e;
		}
	}

	/**
	 * @return
	 */
	public String getOnlineNamesDefault() {
		try {
			String sDid = wipfConfig.getConfParamString("discord_id");
			return wipf.toList(listOnlineNames(sDid));
		} catch (Exception e) {
			return "Fehler 111" + e;
		}
	}

	/**
	 * @param sId
	 * @return
	 */
	public List<String> listOnlineNames(String sId) {
		if (!wipf.isValid(sId)) {
			return null;
		}
		try {
			List<String> lsOnline = new LinkedList<String>();
			String sRes = wipf.httpRequest(httpRequestType.GET,
					"https://discord.com/api/guilds/" + sId + "/widget.json");

			JSONObject jo = new JSONObject(sRes);
			JSONArray ja = (JSONArray) jo.get("members");

			for (Object j : ja) {
				JSONObject o = new JSONObject(j.toString());
				if (o.has("channel_id")) {
					lsOnline.add(o.getString("username"));
				}
			}

			return lsOnline;
		} catch (Exception e) {
			LOGGER.error("Fail: " + e);
			return null;
		}
	}

}
