package org.wipf.jasmarty.logic.checkliste;

import java.util.LinkedList;
import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeItem;
import org.wipf.jasmarty.databasetypes.checkliste.CheckListeListe;
import org.wipf.jasmarty.databasetypes.checkliste.CheckListeVerkn;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class CheckListeVerknService {

	@Inject
	CheckListeListeService cll;
	@Inject
	CheckListeTypeService clt;
	@Inject
	CheckListeItemService cli;

	/**
	 * @param l
	 */
	@Transactional
	public void save(CheckListeVerkn l) {
		l.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		CheckListeVerkn.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<CheckListeVerkn> getAll() {
		return CheckListeVerkn.findAll().list();
	}

	/**
	 * @param cl
	 * @return
	 */
	public List<CheckListeVerkn> getAllByCheckList(CheckListeListe cl) {
		return CheckListeVerkn.getAllByCheckList(cl).list();
	}

	public List<CheckListeVerkn> getByClID(Integer clId) {
		CheckListeListe cl = cll.getById(clId);
		List<CheckListeItem> itemsAlle = new LinkedList<CheckListeItem>();
		// Alle Items des typen laden
		if (cl.types != null) {
			for (String tid : cl.types.split(",")) {
				itemsAlle.addAll(cli.getAllByType(clt.getById((Integer.valueOf(tid)))));
			}
		}

		// item hat jetzt alle Möglichkeiten drin

		// Jetzt alle bisherigen holen
		List<CheckListeVerkn> checkedItemsBisherige = getAllByCheckList(cl);

		List<CheckListeVerkn> resultListe = new LinkedList<CheckListeVerkn>();

		// Matchen
		for (CheckListeItem item : itemsAlle) {
			boolean added = false;
			for (CheckListeVerkn checkItems : checkedItemsBisherige) {
				if (item.id == checkItems.checkListeItem.id) {
					// hier gibt es einen gespeicherten Wert
					resultListe.add(checkItems);
					added = true;
				}
			}
			if (!added) {
				// Keinen wert
				CheckListeVerkn neuerPunkt = new CheckListeVerkn();
				neuerPunkt.checkListeListe = cl;
				neuerPunkt.checkListeItem = item;
				resultListe.add(neuerPunkt);
			}
		}

		return resultListe;
	}

}
