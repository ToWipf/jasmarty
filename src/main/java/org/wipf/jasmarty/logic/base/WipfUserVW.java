package org.wipf.jasmarty.logic.base;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
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
	public void addOrUpdateUser(WipfUser wu) {
		// Password hashen
		wu.password = (BcryptUtil.bcryptHash(wu.password));
		wu.saveOrUpdate();
	}

	/**
	 * @return
	 */
	public List<WipfUser> getAll() {
		return WipfUser.findAll().list();
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

	/**
	 * @param sUsername
	 */
	@Transactional
	public void deleteUser(String sUsername) {
		WipfUser.findByUsername(sUsername).firstResultOptional().ifPresent(o -> {
			o.delete();
		});
	}

}
