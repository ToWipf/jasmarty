package org.wipf.jasmarty.databasetypes.telegram;

import java.io.Serializable;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Wipf
 *
 */
@Entity
@RegisterForReflection
@Table(name = "telegram_usercache")
public class Usercache extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("telegram_usercache");

	@Id
	public Long chatid;
	public String msg; // LastMSG TODO rename
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
				LOGGER.info("Neuer Telegram User: " + this.toString());
				this.persist();
			}
		} else {
			// Neu
			this.persist();
		}
	}

}
