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
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "titel", nullable = true)
	public String titel;
	@Column(name = "art", nullable = true)
	public String art;
	@Column(name = "typ", nullable = true)
	public String typ;
	@Column(name = "gesehendate", nullable = true)
	public Integer gesehendate;
	@Column(name = "infotext", nullable = true)
	public String infotext;
	@Column(name = "bewertung", nullable = true)
	public Integer bewertung;
	@Column(name = "editby", nullable = true)
	public String editby;
	@Column(name = "date", nullable = true)
	public Integer date;

	/**
	 *
	 */
	@Override
	public String toString() {
		// TODO
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
