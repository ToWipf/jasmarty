package org.wipf.jasmarty.logic.checkliste;

import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeListe;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class CheckListeListeService {

	/**
	 * @param l
	 */
	@Transactional
	public void save(CheckListeListe l) {
		l.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		CheckListeListe.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<CheckListeListe> getAll() {
		return CheckListeListe.findAll().list();
	}

}
