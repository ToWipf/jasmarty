package org.wipf.jasmarty.databasetypes.daylog;

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
@Table(name = "DaylogTextEvent")
public class DaylogEvent extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "dateid", nullable = false)
	public Integer dateid;
	@Column(name = "typ", nullable = false)
	public String typid;
	@Column(name = "text", nullable = true)
	public String text;

	@Override
	public String toString() {
		return "id=" + id + ", dateid=" + dateid + ", typ=" + typid + ", text=" + text;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			DaylogEvent existingData = DaylogEvent.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.dateid = this.dateid;
				existingData.typid = this.typid;
				existingData.text = this.text;
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
	 * @param sType
	 * @return
	 */
	public static PanacheQuery<DaylogEvent> findByTypeId(String sType) {
		return find("select e from DaylogEvent e where typ =?1", sType);
	}

	/**
	 * @param nId
	 * @return
	 */
	public static PanacheQuery<DaylogEvent> findByDateId(Integer nDateId) {
		return find("select e from DaylogEvent e where dateid =?1", nDateId);
	}

	/**
	 * @param sType
	 * @return
	 */
	public static PanacheQuery<DaylogEvent> findByINTypeANDText(String sType, String sText) {
		return find("select e from DaylogEvent e where typ =?1 AND text LIKE ?2", sType, "%" + sText + "%");
	}

	// "SELECT COUNT(*) anz, * from daylogTextEvent where typ IN (" + sTypIds +
	// ")GROUP by text ORDER by anz DESC";

	public static PanacheQuery<DaylogEvent> findByTypeIds(String sTypes) {
		return find("select e from DaylogEvent e where typ IN ?1 GROUP by text ORDER by anz DESC", sTypes);
	}

}
