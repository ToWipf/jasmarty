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

/**
 * @author wipf
 *
 */
@Entity
@Table(name = "base_authKey")
public class AuthKey extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	@Column(name = "key", nullable = false)
	public String key;

	@Override
	public String toString() {
		return "id=" + id + " key=" + key;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			AuthKey existingData = AuthKey.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.key = this.key;
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

	public static PanacheQuery<AuthKey> findByKey(String sKey) {
		return find("select authKey from authKey authKey where authKey.name =?1", sKey);
	}

}
