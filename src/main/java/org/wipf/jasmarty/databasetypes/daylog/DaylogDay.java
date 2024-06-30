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
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "daylogDay")
public class DaylogDay extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public Integer id;
	@Column(name = "date", nullable = false, unique = true)
	public String date;
	@Column(name = "tagestext", nullable = false)
	public String tagestext;

	@Override
	public String toString() {
		return "id=" + id + " date=" + date + " tagestext" + tagestext;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			DaylogDay existingData = DaylogDay.findById(id);
			if (existingData != null) {
				// Update
				existingData.date = this.date;
				existingData.tagestext = this.tagestext.trim();
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.err.println("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.tagestext = this.tagestext.trim();
			this.persist();
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<DaylogDay> findByDate(String date) {
		return find("select e from DaylogDay e where date = ?1", date);
	}

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<DaylogDay> findByLikeDate(String date) {
		return find("select e from DaylogDay e where date LIKE ?1", "%" + date + "%");
	}

}
