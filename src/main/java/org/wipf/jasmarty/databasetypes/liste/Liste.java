package org.wipf.jasmarty.databasetypes.liste;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

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

}
