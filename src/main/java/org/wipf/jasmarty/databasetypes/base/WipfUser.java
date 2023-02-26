package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;
import org.wipf.jasmarty.WipfException;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "base_users")
public class WipfUser extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username", nullable = false, unique = true)
	public String username;
	@Column(name = "password", nullable = false)
	public String password;
	@Column(name = "role", nullable = false)
	public String role;

	@Override
	public String toString() {
		return "username=" + username + ", password=" + password + ", role=" + role;
	}

	/**
	 * @param sJson
	 * @return
	 * @throws WipfException
	 */
	public void saveByJson(String sJson) throws WipfException {
		JSONObject jo = new JSONObject(sJson);
		String sUsern = jo.getString("username").trim();
		checkUsername(sUsern);

		WipfUser wu = WipfUser.findByUsername(sUsern).firstResult();

		if (wu == null) {
			System.out.println("NEU");
			// Neu
			username = sUsern;
			password = (BcryptUtil.bcryptHash(jo.getString("password")));
			role = (jo.getString("role"));
			this.persist();

		} else {
			// Update
			System.out.println("UPDATE");
			wu.password = (BcryptUtil.bcryptHash(jo.getString("password")));
			wu.role = (jo.getString("role"));
			wu.persist();
		}
	}

	/**
	 * @param sUsername
	 * @return
	 */
	public static PanacheQuery<WipfUser> findByUsername(String sUsername) {
		return find("select e from WipfUser e where username =?1", sUsername);
	}

	/**
	 * @param sUsername
	 * @return
	 * @throws WipfException
	 */
	private static void checkUsername(String sUsername) throws WipfException {
		if (sUsername.contains(" ")) {
			throw new WipfException("Keine Leerzeichen im Benutzernamen erlaubt");
		}
	}

}
