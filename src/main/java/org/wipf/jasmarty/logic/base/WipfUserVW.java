package org.wipf.jasmarty.logic.base;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

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
	public void crateDefaultUser() {
		WipfUser wu = new WipfUser();
		wu.id = 1;
		wu.username = "admin";
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.password = BcryptUtil.bcryptHash("jadmin");
		// wu.setPassword("jadmin");
		wu.role = "admin";
		wu.saveOrUpdate();
	}

	/**
	 * @return
	 */
	@Transactional
	public void crateHealthCheckUser() {
		WipfUser wu = new WipfUser();
		wu.id = 2;
		wu.username = "check";
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.password = BcryptUtil.bcryptHash("check");
		// wu.setPassword("check");
		wu.role = "check";
		wu.saveOrUpdate();
	}

}
