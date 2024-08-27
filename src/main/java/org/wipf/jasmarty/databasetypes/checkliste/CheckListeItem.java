package org.wipf.jasmarty.databasetypes.checkliste;

import java.io.Serializable;

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
@Table(name = "checkListeItem")
public class CheckListeItem extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "item", nullable = false)
	public String item;
	@Column(name = "prio", nullable = false)
	public Integer prio;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type", nullable = false)
	public CheckListeType checkListeType;

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
				existingData.prio = this.prio;
				existingData.checkListeType = this.checkListeType;
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

	/**
	 * @param t
	 * @return
	 */
	public static PanacheQuery<PanacheEntityBase> findByType(CheckListeType t) {
		return find("select e from CheckListeItem e where e.checkListeType = ?1", t);
	}

}
