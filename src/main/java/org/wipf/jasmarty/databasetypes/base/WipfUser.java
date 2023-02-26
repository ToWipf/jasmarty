package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.wipf.jasmarty.WipfException;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

/**
 * @author wipf
 *
 */
@Entity
@Table(name = "base_users")
public class WipfUser extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "username", nullable = false, unique = true)
	public String username;
	@Column(name = "password", nullable = false)
	public String password;
	@Column(name = "role", nullable = false)
	public String role;

	public static void add(String username, String password, String role) throws WipfException {
		if (username.contains(" ")) {
			throw new WipfException("Keine Leerzeichen im Benutzernamen erlaubt");
		}

		WipfUser wu = new WipfUser();
		wu.username = username.trim();
		wu.password = BcryptUtil.bcryptHash(password);
		wu.role = role;
		wu.persist();

	}

	public static PanacheQuery<WipfUser> findByUsername(String sUsername) {
		return find("select * from base_users where username =?1", sUsername);
	}

}
