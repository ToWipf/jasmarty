package org.wipf.jasmarty.logic.daylog;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogDayDB {

	/**
	 * @param o
	 */
	@Transactional
	public void save(DaylogDay o) {
		o.saveOrUpdate();
	}

	/**
	 * @param sDate
	 * @return
	 */
	public DaylogDay get(String sDate) {
		return DaylogDay.findByDate(sDate).singleResult();
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
		System.out.println(sDateQuery);
		System.out.println(DaylogDay.findByLikeDate(sDateQuery).list().size());
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
