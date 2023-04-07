package org.wipf.jasmarty.databasetypes.liste;

import java.io.Serializable;

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
@Table(name = "rndEvent")
public class RndEvent extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String eventtext;
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

	/**
	 * @param
	 * @return
	 */
	public static PanacheQuery<RndEvent> findRND() {
		return find("select e from RndEvent e where active = 1 ORDER BY RANDOM()");
	}

}