package org.wipf.jasmarty.logic.base;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.databasetypes.base.WipfUser;

import io.quarkus.elytron.security.common.BcryptUtil;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class WipfUserVW {

	private static final Logger LOGGER = Logger.getLogger("WipfUserVW");

	/**
	 * @param sJson
	 */
	@Transactional
	public void addOrUpdateUser(String sJson) {
		new WipfUser().setByJson(sJson).saveOrUpdate();
	}

	/**
	 * @param sUsername
	 */
	@Transactional
	public void deleteUser(String sUsername) {
		WipfUser.findByUsername(sUsername).firstResultOptional().ifPresent(wu -> {
			wu.delete();
		});
	}

	/**
	 * @return
	 */
	public JSONArray getAllUsersAsJson() {
		JSONArray a = new JSONArray();
		List<WipfUser> wul = WipfUser.findAll().list();
		wul.forEach(wu -> {
			JSONObject o = new JSONObject();
			o.put("id", wu.id);
			o.put("username", wu.username);
			o.put("password", wu.password);
			o.put("role", wu.role);
			a.put(o);
		});
		return a;
	}

	/**
	 * @return
	 */
	@Transactional
	public void crateDefaultUsers() {
		if (WipfUser.findByUsername("admin").count() == 0) {
			WipfUser wuAdmin = new WipfUser();
			wuAdmin.username = "admin";
			wuAdmin.password = BcryptUtil.bcryptHash("jadmin");
			wuAdmin.role = "admin";
			wuAdmin.saveOrUpdate();
			LOGGER.info("Erstelle Admin User: " + wuAdmin.toString());
		}
		if (WipfUser.findByUsername("check").count() == 0) {
			WipfUser wuCheck = new WipfUser();
			wuCheck.username = "check";
			wuCheck.password = BcryptUtil.bcryptHash("check");
			wuCheck.role = "check";
			wuCheck.saveOrUpdate();
			LOGGER.info("Erstelle check User: " + wuCheck.toString());
		}
	}

}
