package org.wipf.jasmarty.databasetypes.checkliste;

import java.io.Serializable;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
	private static final Logger LOGGER = Logger.getLogger("checkListeVerkn");

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
	public Boolean checked;

	/**
	 *
	 */
	@Override
	public String toString() {
		return "id=" + id + ", data=" + checkListeListe + ", checkListeItem=" + checkListeItem;
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
				existingData.checked = this.checked;
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

	/**
	 * Nur Updaten, keine neue ID vergeben
	 */
	public void updateOnly() {
		if (this.id != null) {
			CheckListeVerkn existingData = CheckListeVerkn.findById(this.id);

			// Update
			existingData.checkListeListe = this.checkListeListe;
			existingData.checkListeItem = this.checkListeItem;
			existingData.checked = this.checked;
			existingData.persist();
		} else {
			LOGGER.info("updateOnly Fails, Button Spam?");
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public static PanacheQuery<PanacheEntityBase> getAllByCheckList(CheckListeListe cl) {
		return find("select e from CheckListeVerkn e where e.checkListeListe =?1", cl);
	}

}
