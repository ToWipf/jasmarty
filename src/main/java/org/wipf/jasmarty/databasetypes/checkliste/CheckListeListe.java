package org.wipf.jasmarty.databasetypes.checkliste;

import java.io.Serializable;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
@Table(name = "checkListeListe")
public class CheckListeListe extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "listenname", nullable = false)
	public String listenname;
	@Column(name = "date", nullable = false)
	public String date;
	@Column(name = "types", nullable = true)
	public Integer[] types;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", data=" + listenname;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			CheckListeListe existingData = CheckListeListe.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.listenname = this.listenname.trim();
				existingData.date = this.date;
				existingData.types = this.types;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.err.println("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.listenname = this.listenname.trim();
			this.persist();
		}
	}

}
