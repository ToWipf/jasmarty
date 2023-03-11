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
@Table(name = "listeType")
public class ListeType extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "typename", nullable = false, unique = true)
	public String typename;

	@Override
	public String toString() {
		return "id=" + id + ", typename=" + typename;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			ListeType existingData = ListeType.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.typename = this.typename;
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
