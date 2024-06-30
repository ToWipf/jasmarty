package org.wipf.jasmarty.logic.listen;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.liste.Medien;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MedienService {

	@Inject
	Wipf wipf;

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueMedien(Telegram t) {
		String sAction = t.getMessageStringPartLow(1);
		if (sAction == null) {
			// @formatter:off
				return 
					"text (add) titel" + "\n" + 
					"delete ID" + "\n" +
					"list" +  "\n" +
					"count";
			// @formatter:on
		}

		switch (sAction) {
		case "del":
		case "delete":
			del(t.getMessageIntPart(2));
			return "ok";
		case "l":
		case "list":
			return getAllAsList();
		default:
			return saveItem(t).toString();
		}
	}

	/**
	 * @return
	 */
	public String getAllAsList() {
		StringBuilder sb = new StringBuilder();

		for (Medien tItem : getAll()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}

			sb.append(tItem.titel + "\n");
			sb.append(tItem.infotext + "\n");
			sb.append(tItem.date + "\n");
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * @return
	 * 
	 */
	public List<Medien> getAll() {
		return Medien.findAll().list();
	}

	/**
	 * @param medienE
	 * @return id
	 */
	@Transactional
	public void saveItem(Medien medienE) {
		medienE.saveOrUpdate();
	}

	/**
	 * hier kann nur der titel gesetzt werden
	 * 
	 * @param t
	 * @return
	 */
	private Integer saveItem(Telegram t) {
		Medien medienE = new Medien();
		medienE.titel = (t.getMessageFullWithoutFirstWord());
		medienE.editby = (t.getFromIdOnly().toString());
		medienE.date = (t.getDate());
		medienE.gesehendate = (0);
		medienE.bewertung = (0);
		saveItem(medienE);
		// TODO
		return -1;
	}

	/**
	 * @param nId
	 */
	@Transactional
	public void del(Integer nId) {
		Medien.findById(nId).delete();
	}

}
