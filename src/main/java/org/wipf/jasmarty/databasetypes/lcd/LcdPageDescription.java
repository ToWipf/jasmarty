package org.wipf.jasmarty.databasetypes.lcd;

import java.io.Serializable;

import org.jboss.logging.Logger;
import org.json.JSONArray;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
@Table(name = "lcd_pages")
public class LcdPageDescription extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("lcd_pages");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String name;
	public Integer timeoutTime;
	public String staticData;
	public String dynamicData;

	public void saveOrUpdate() {
		if (this.id != null) {
			LcdPageDescription existingData = LcdPageDescription.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.name = this.name;
				existingData.timeoutTime = this.timeoutTime;
				existingData.staticData = this.staticData;
				existingData.dynamicData = this.dynamicData;
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
	 * @return
	 */
	public JSONArray getStaticData() {
		if (staticData == null || staticData.equals("")) {
			return new JSONArray();
		}
		return new JSONArray(staticData);
	}

	/**
	 * @return
	 */
	public JSONArray getDynamicData() {
		if (dynamicData == null || dynamicData.equals("")) {
			return new JSONArray();
		}
		return new JSONArray(dynamicData);
	}

	/**
	 * @param o
	 */
	public void setStaticData(JSONArray o) {
		staticData = o.toString();
	}

	/**
	 * @param o
	 */
	public void setDynamicData(JSONArray o) {
		dynamicData = o.toString();
	}
}
