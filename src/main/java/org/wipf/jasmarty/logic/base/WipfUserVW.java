package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;
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
	 * @param user
	 * @param bAuthDb
	 * @throws SQLException
	 */
	@Transactional
	public void addOrUpdateUser(WipfUser user) {
		user.persist();
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

	@Transactional
	public void initDB() {

		crateHealthCheckUser();
		crateDefaultUser();

	}

	/**
	 * @return
	 */
	private void crateDefaultUser() {
		WipfUser wu = new WipfUser();
		wu.username = "admin";
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.password = BcryptUtil.bcryptHash("jadmin");
		// wu.setPassword("jadmin");
		wu.role = "admin";
		wu.persist();
	}

	/**
	 * @return
	 */
	private void crateHealthCheckUser() {
		WipfUser wu = new WipfUser();
		wu.username = "check";
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.password = BcryptUtil.bcryptHash("check");
		// wu.setPassword("check");
		wu.role = "check";
		wu.persist();
	}

	public JSONArray getAllUsersAsJson() {

		JSONArray a = new JSONArray();

		List<WipfUser> wul = WipfUser.findAll().list();

		wul.forEach(wu -> {

			JSONObject o = new JSONObject();
			o.put("username", wu.username);
			o.put("password", wu.password);
			o.put("role", wu.role);
			a.put(o);
		});

		return a;
	}

}
