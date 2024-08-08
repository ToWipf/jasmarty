package org.wipf.jasmarty.logic.listen;

import java.util.List;

import org.wipf.jasmarty.databasetypes.liste.ListeType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
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
