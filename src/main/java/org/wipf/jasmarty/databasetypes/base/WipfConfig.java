package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "config")
public class WipfConfig extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "key", nullable = false)
	public String key;
	@Column(name = "val")
	public String value;

	@Override
	public String toString() {
		return "key=" + key + " value=" + value;
	}

	/**
	 * @param sKey
	 * @param sValue
	 * @return
	 */
	public WipfConfig setBy(String sKey, String sValue) {
		this.key = sKey;
		this.value = sValue;
		return this;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.key != null) {
			WipfConfig existingData = WipfConfig.findById(this.key);
			if (existingData != null) {
				// Update
				existingData.value = this.value;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.out.println("Neue Config: " + this.toString());
				this.persist();
			}
		} else {
			// Neu
			this.persist();
		}
	}

	/**
	 * @param sKey
	 * @return
	 */
	public static PanacheQuery<WipfConfig> findByKey(String sKey) {
		return find("select e from WipfConfig e where key =?1", sKey);
	}

}
