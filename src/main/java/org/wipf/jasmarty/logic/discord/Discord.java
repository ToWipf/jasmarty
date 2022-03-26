package org.wipf.jasmarty.logic.discord;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.Wipf.httpRequestType;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class Discord {

	@Inject
	Wipf wipf;

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
			e.printStackTrace();
			return null;
		}
	}

}
