package org.wipf.jasmarty.databasetypes.liste;

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
 * @author wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "rndEvent")
public class RndEvent extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "eventtext", nullable = false)
	public String eventtext;
	@Column(name = "active", nullable = false)
	public Boolean active;

	@Override
	public String toString() {
		return "id=" + id + ", eventtext=" + eventtext + ", active=" + active;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			RndEvent existingData = RndEvent.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.eventtext = this.eventtext;
				existingData.active = this.active;
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