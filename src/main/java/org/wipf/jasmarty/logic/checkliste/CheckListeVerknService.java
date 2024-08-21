package org.wipf.jasmarty.logic.checkliste;

import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeVerkn;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class CheckListeVerknService {

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

}
