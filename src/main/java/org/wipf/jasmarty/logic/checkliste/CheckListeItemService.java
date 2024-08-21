package org.wipf.jasmarty.logic.checkliste;

import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeItem;

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

}
