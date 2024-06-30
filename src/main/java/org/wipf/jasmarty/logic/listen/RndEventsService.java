package org.wipf.jasmarty.logic.listen;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;

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
	 * 
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
			case "g":
			case "e":
			case "get":
			default:
				return getRndEventRnd();
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
	public String getRndEventRnd() {
		return RndEvent.findRND().firstResult().eventtext;
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	@Transactional
	public void save(RndEvent r) {
		r.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
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
