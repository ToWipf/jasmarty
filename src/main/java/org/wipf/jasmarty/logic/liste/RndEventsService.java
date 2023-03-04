package org.wipf.jasmarty.logic.liste;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.databasetypes.liste.RndEvent;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.tasks.RndTask;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class RndEventsService {

	@Inject
	RndTask rndTask;

	private static final Logger LOGGER = Logger.getLogger("Telegram rndEvent");

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String menueRndEvent(Telegram t) {
		try {
			String sAction = t.getMessageStringPartLow(1);
			if (sAction == null) {
				return "Anleitung mit rndEvent Hilfe";
			}

			switch (sAction) {
			case "on":
			case "start":
				return "Aktiv: " + rndTask.startRndTask();
			case "aus":
			case "stop":
			case "off":
				return "Aktiv: " + rndTask.stopRndTask();
//			case "a":
//			case "add":
//				return save(t);
//			case "del":
//				return del(t.getMessageIntPart(1));
//			case "list":
//				return getAllRndEvent();
			case "c":
			case "count":
				return count().toString();
			case "g":
			case "e":
			case "get":
				return getRndEventRnd();
			default:
				return
			//@formatter:off
					"rndEvent Add: rndEvent hinzufügen\n" +
//					"rndEvent Del: id löschen\n" + 
//					"rndEvent List: alles auflisten\n" +
					"rndEvent Get: ZufallsrndEvent\n" +
					"rndEvent Count: Anzahl der Einträge\n";
			//@formatter:on
			}
		} catch (Exception e) {
			LOGGER.error("menueRndEvent");
			e.printStackTrace();
			return "Fehler 114: " + e;
		}
	}

	/**
	 * @return
	 */
	public String getRndEventRnd() throws SQLException {
//		String sQuery = "select eventtext from rndEvent WHERE active = 1 ORDER BY RANDOM() LIMIT 1;";
//
//		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
//		while (rs.next()) {
//			// Es gibt nur einen Eintrag
//			return (rs.getString("eventtext"));
//		}
//		return null;
		return "TODO";
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public void save(RndEvent r) {
		r.saveOrUpdate();
	}

	/**
	 * @return
	 */
	public Integer count() {
		return getAll().size();
	}

	/**
	 * @param t
	 * @return
	 */
	public void del(Integer nId) {
		RndEvent.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<RndEvent> getAll() {
		return RndEvent.findAll().list();
	}

}
