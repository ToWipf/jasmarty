package org.wipf.jasmarty.databasetypes.liste;

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
@Table(name = "medien")
public class Medien extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String titel;
	public String art;
	public String typ;
	public Integer gesehendate;
	public String infotext;
	public Integer bewertung;
	public String editby;
	public Integer date;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", titel=" + titel;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			Medien existingData = Medien.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.titel = this.titel;
				existingData.art = this.art;
				existingData.typ = this.typ;
				existingData.gesehendate = this.gesehendate;
				existingData.infotext = this.infotext;
				existingData.bewertung = this.bewertung;
				existingData.editby = this.editby;
				existingData.date = this.date;
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
