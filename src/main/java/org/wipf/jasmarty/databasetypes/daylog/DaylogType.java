package org.wipf.jasmarty.databasetypes.daylog;

import java.io.Serializable;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


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
	@Column(name = "color", nullable = true)
	public String color;
	@Column(name = "prio", nullable = true)
	public Integer prio;

	@Override
	public String toString() {
		return "id=" + id + ", type=" + type + ", art=" + art + ", color=" + color + ", prio=" + prio;
	}

	public Integer getPrio() {
		return prio;
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
				existingData.color = this.color;
				existingData.prio = this.prio;
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
