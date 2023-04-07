package org.wipf.jasmarty.databasetypes.telegram;

import java.io.Serializable;

import javax.persistence.Entity;
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
@Table(name = "telegram_usercache")
public class Usercache extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	public Long chatid;
	public String msg;
	public String usercache;
	public Integer counter;

	@Override
	public String toString() {
		return "chatid=" + chatid + ", msg=" + msg + ", usercache=" + usercache + ", counter=" + counter;
	}

	/**
	 * 
	 */
	public void saveOrUpdate() {
		if (this.chatid != null) {
			Usercache existingData = Usercache.findById(this.chatid);
			if (existingData != null) {
				// Update
				existingData.msg = this.msg;
				existingData.usercache = this.usercache;
				existingData.counter = this.counter;
				existingData.persist();
			} else {
				// Neu mit unbekannter id
				System.out.println("Neuer Telegram User: " + this.toString());
				this.persist();
			}
		} else {
			// Neu
			this.persist();
		}
	}

}
