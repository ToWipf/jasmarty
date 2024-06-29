package org.wipf.jasmarty.logic.listen;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.liste.ListeType;

/**
 * @author Wipf
 *
 */
@RequestScoped
public class ListeTypeService {

	/**
	 * @param o
	 * @return
	 */
	@Transactional
	public void save(ListeType o) {
		o.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		ListeType.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<ListeType> getAll() {
		return ListeType.findAll().list();
	}

}
