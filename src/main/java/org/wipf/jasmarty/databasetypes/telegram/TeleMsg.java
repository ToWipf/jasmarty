package org.wipf.jasmarty.databasetypes.telegram;

import java.io.Serializable;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
@Table(name = "telegram_msg")
public class TeleMsg extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("telegram_msg");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Long id;
	@Column(name = "frage", nullable = false)
	public String frage;
	public String antwort;

	@Override
	public String toString() {
		return "id=" + id + ", frage=" + frage + ", antwort=" + antwort;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			TeleMsg existingData = TeleMsg.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.frage = this.frage;
				existingData.frage = this.frage;
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
	public static PanacheQuery<TeleMsg> findByFrage(String frage) {
		return find("select e from TeleMsg e where frage =?1", frage);
	}

}
