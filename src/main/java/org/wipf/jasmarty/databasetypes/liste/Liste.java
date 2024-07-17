package org.wipf.jasmarty.databasetypes.liste;

import java.io.Serializable;

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
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "liste")
public class Liste extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "data", nullable = false)
	public String data;
	@Column(name = "typeid", nullable = false)
	public Integer typeid;
	@Column(name = "date", nullable = false)
	public String date;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", data=" + data + ", typeid=" + typeid;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			Liste existingData = Liste.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.data = this.data.trim();
				existingData.typeid = this.typeid;
				existingData.date = this.date;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.err.println("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.data = this.data.trim();
			this.persist();
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<Liste> findByType(Integer nTypeId) {
		return find("select e from Liste e where typeid =?1", nTypeId);
	}

	/**
	 * Das neueste Datum oben
	 * 
	 * @param nAnzahl
	 * @return
	 */
	public static PanacheQuery<Liste> findLast() {
		return find("select e from Liste e  ORDER by date DESC");
	}

}
