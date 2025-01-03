package org.wipf.jasmarty.databasetypes.liste;

import java.io.Serializable;

import org.jboss.logging.Logger;

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
@Table(name = "listeType")
public class ListeType extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("listeType");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "typename", nullable = false, unique = true)
	public String typename;
	@Column(name = "color", nullable = true)
	public String color;
	@Column(name = "showOverview", nullable = true)
	public Boolean showOverview;

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
				existingData.color = this.color;
				existingData.showOverview = this.showOverview;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				LOGGER.warn("ID nicht in DB! " + this.toString());
			}
		} else {
			// Neu
			this.persist();
		}
	}
}
