package org.wipf.jasmarty.logic.daylog;

import java.util.List;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@RequestScoped
public class DaylogDayService {

	/**
	 * @param o
	 */
	@Transactional
	public DaylogDay save(DaylogDay o) {
		o.saveOrUpdate();
		return o;
	}

	/**
	 * @param sDate
	 * @return
	 */
	@Transactional
	public DaylogDay get(String sDate) {
		List<DaylogDay> l = DaylogDay.findByDate(sDate).list();
		if (l.size() == 1) {
			return l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param nId
	 * @return
	 */
	public DaylogDay getById(Integer nId) {
		return DaylogDay.findById(nId);
	}

	/**
	 * @param sDateQuery
	 * @return
	 */
	public List<DaylogDay> getAllByDateQuery(String sDateQuery) {
		return DaylogDay.findByLikeDate(sDateQuery).list();
	}

	/**
	 * @param nUserId
	 * @return
	 */
	public List<DaylogDay> getAll() {
		return DaylogDay.findAll().list();
	}

	/**
	 * @param nId
	 */
	@Transactional
	public void del(Integer nId) {
		DaylogDay.findById(nId).delete();
	}

}
