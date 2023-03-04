package org.wipf.jasmarty.databasetypes.telegram;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "telemsg")
public class TeleMsg extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Long id;
	@Column(name = "request", nullable = false)
	public String request;
	@Column(name = "response", nullable = true)
	public String response;
	@Column(name = "editby", nullable = true)
	public String editby;
	@Column(name = "date", nullable = true)
	public Integer date;

	@Override
	public String toString() {
		return "id=" + id + ", request=" + request + ", response=" + response + ", date=" + date;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			TeleMsg existingData = TeleMsg.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.request = this.request;
				existingData.response = this.response;
				existingData.editby = this.editby;
				existingData.date = this.date;
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

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<TeleMsg> findByRequest(String sRequest) {
		return find("select e from Liste e where request =?1", sRequest);
	}

}
