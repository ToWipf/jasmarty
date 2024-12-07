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
@Table(name = "checkListeType")
public class CheckListeType extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "type", nullable = false)
	public String type;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", type=" + type;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			CheckListeType existingData = PanacheEntityBase.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.type = this.type.trim();
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
