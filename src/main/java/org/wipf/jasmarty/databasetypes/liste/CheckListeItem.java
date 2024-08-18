package org.wipf.jasmarty.databasetypes.liste;

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
@Table(name = "checkListeItem")
public class CheckListeItem extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "item", nullable = false)
	public String item;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", data=" + item;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			CheckListeItem existingData = CheckListeItem.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.item = this.item.trim();
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.err.println("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.item = this.item.trim();
			this.persist();
		}
	}

}
