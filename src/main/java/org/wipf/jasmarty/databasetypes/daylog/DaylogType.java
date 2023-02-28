package org.wipf.jasmarty.databasetypes.daylog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "daylogType")
public class DaylogType extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "type", nullable = false)
	public String type;
	@Column(name = "art", nullable = false)
	public String art;

	@Override
	public String toString() {
		return "id=" + id + ", type=" + type + ", art=" + art;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			DaylogType existingData = DaylogType.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.type = this.type;
				existingData.art = this.art;
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

}
