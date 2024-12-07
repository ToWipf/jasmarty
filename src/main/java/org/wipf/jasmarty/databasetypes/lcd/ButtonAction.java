package org.wipf.jasmarty.databasetypes.lcd;

import java.io.Serializable;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "lcd_actions")
public class ButtonAction extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("lcd_actions");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public Integer button;
	public Boolean active;
	public String action;

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			ButtonAction existingData = ButtonAction.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.button = this.button;
				existingData.active = this.active;
				existingData.action = this.action;
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
	 * @param date
	 * @return
	 */
	public static PanacheQuery<ButtonAction> findByButton(Integer nButton) {
		return find("select e from ButtonAction e where button =?1", nButton);
	}
}
