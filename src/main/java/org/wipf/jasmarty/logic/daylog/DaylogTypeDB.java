package org.wipf.jasmarty.logic.daylog;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.daylog.DaylogType;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogTypeDB {

	/**
	 * @param nId
	 * @return
	 */
	public List<DaylogType> get(Integer nId) {
		return DaylogType.findById(nId);
	}

	/**
	 * @return
	 */
	public List<DaylogType> getAll() {
		return DaylogType.findAll().list();
	}

	@Transactional
	public void save(DaylogType o) {
		o.saveOrUpdate();
	}

	@Transactional
	public void del(Integer nId) {
		DaylogType.findById(nId).delete();
	}

}
