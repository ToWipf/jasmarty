package org.wipf.jasmarty.databasetypes.telegram;

import java.io.Serializable;

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
@Table(name = "telegram_log")
public class TeleLog extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	public Integer msgid;
	public String frage;
	@Column(name = "antwort", nullable = true, length = 9999)
	public String antwort;
	public Long chatid;
	public String msgfrom;
	public Integer date;

	@Override
	public String toString() {
		return "msgid=" + msgid + ", frage=" + frage + ", msgfrom=" + msgfrom + ", antwort=" + antwort;
	}

	/**
	 * @param date
	 * @return
	 */
	public static PanacheQuery<TeleLog> findByRequest(String sRequest) {
		return find("select e from Liste e where request =?1", sRequest);
	}

}
