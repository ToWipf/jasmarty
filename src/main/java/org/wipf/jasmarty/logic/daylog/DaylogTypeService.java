package org.wipf.jasmarty.logic.daylog;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.daylog.DaylogType;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogTypeService {

	/**
	 * @param nId
	 * @return
	 */
	public DaylogType get(Integer nId) {
		return DaylogType.findById(nId);
	}

	/**
	 * @return
	 */
	public List<DaylogType> getAll() {
		return DaylogType.findAll().list();
	}

	/**
	 * @param o
	 */
	@Transactional
	public void save(DaylogType o) {
		o.saveOrUpdate();
	}

	/**
	 * @param nId
	 */
	@Transactional
	public void del(Integer nId) {
		DaylogType.findById(nId).delete();
	}

}
