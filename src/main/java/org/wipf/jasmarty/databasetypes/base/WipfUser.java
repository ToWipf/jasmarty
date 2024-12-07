package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "base_users")
public class WipfUser extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("base_users");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "username", nullable = false, unique = true)
	public String username;
	@Column(name = "password", nullable = false)
	public String password;
	@Column(name = "role", nullable = false)
	public String role;

	@Override
	public String toString() {
		return "id=" + id + ", username=" + username + ", password=" + password + ", role=" + role;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			WipfUser existingData = WipfUser.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.username = this.username;
				existingData.password = this.password;
				existingData.role = this.role;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				LOGGER.warn("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.persist();
		}
	}

	/**
	 * @param sUsername
	 * @return
	 */
	public static PanacheQuery<WipfUser> findByUsername(String sUsername) {
		return find("select e from WipfUser e where username =?1", sUsername);
	}

}
