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
@Table(name = "telegramlog")
public class TeleLog extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "msgid", nullable = false)
	public Long msgid;
	@Column(name = "msg", nullable = true)
	public String msg;
	@Column(name = "antw", nullable = true)
	public String antw;
	@Column(name = "chatid", nullable = true)
	public String chatid;
	@Column(name = "msgfrom", nullable = true)
	public String msgfrom;
	@Column(name = "msgdate", nullable = true)
	public Integer msgdate;
	@Column(name = "type", nullable = true)
	public String type;

	@Override
	public String toString() {
		return "msgid=" + msgid + ", msg=" + msg + ", antw=" + antw + ", msgfrom=" + msgfrom;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.id != null) {
			TeleLog existingData = TeleLog.findById(this.id);
			if (existingData != null) {
				// Update
				existingData.msgid = this.msgid;
				existingData.msg = this.msg;
				existingData.antw = this.antw;
				existingData.chatid = this.chatid;
				existingData.msgfrom = this.msgfrom;
				existingData.msgdate = this.msgdate;
				existingData.type = this.type;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.err.println("ID nicht in DB! " + this.toString());
			}
		} else {
			if (this.msgid == null) {
				this.msgid = -1l;
			}
			// Neu
			this.persist();
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<TeleLog> findByRequest(String sRequest) {
		return find("select e from Liste e where request =?1", sRequest);
	}

}
