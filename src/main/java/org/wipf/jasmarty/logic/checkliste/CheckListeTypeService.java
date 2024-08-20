package org.wipf.jasmarty.logic.checkliste;

import java.util.List;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeType;
import org.wipf.jasmarty.logic.base.Wipf;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class CheckListeTypeService {

	@Inject
	Wipf wipf;

	/**
	 * @param l
	 */
	@Transactional
	public void save(CheckListeType l) {
		l.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		CheckListeType.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<CheckListeType> getAll() {
		return CheckListeType.findAll().list();
	}

}
