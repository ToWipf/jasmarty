package org.wipf.jasmarty.databasetypes.checkliste;

import java.io.Serializable;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "checkListeVerkn")
public class CheckListeVerkn extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checkListeListe", nullable = false)
	public CheckListeListe checkListeListe;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checkListeItem", nullable = false)
	public CheckListeItem checkListeItem;
	@Column(name = "checked", nullable = false)
	public Boolean check;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", data=" + checkListeListe + ", typeid=" + checkListeItem;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			CheckListeVerkn existingData = CheckListeVerkn.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.checkListeListe = this.checkListeListe;
				existingData.checkListeItem = this.checkListeItem;
				existingData.check = this.check;
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
