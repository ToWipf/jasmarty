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
@Table(name = "telegram_log")
public class TeleLog extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Integer id;
	@Column(name = "msgid", nullable = false)
	public Long msgid;
	@Column(name = "frage", nullable = true)
	public String frage;
	@Column(name = "antwort", nullable = true)
	public String antwort;
	@Column(name = "chatid", nullable = true)
	public String chatid;
	@Column(name = "msgfrom", nullable = true)
	public String msgfrom;
	@Column(name = "msgdate", nullable = true)
	public Integer msgdate;

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
