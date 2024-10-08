package org.wipf.jasmarty.logic.checkliste;

import java.util.Comparator;
import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeItem;
import org.wipf.jasmarty.databasetypes.checkliste.CheckListeType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class CheckListeItemService {

	/**
	 * @param l
	 */
	@Transactional
	public void save(CheckListeItem l) {
		l.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		CheckListeItem.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<CheckListeItem> getAll() {
		return CheckListeItem.findAll().list();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<CheckListeItem> getAllByType(CheckListeType t) {

		// Gleich nach prio Sortieren
		List<CheckListeItem> cli = CheckListeItem.findAllByType(t).list();
		cli.sort(Comparator.comparingInt(o -> o.prio));
		return cli;

		// return CheckListeItem.findAllByType(t).list();
	}

}
