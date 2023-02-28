package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
				System.err.println("ID nicht in DB! " + this.toString());
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
