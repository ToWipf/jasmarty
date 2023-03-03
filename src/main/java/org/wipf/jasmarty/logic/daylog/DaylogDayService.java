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
public class DaylogDayService {

	/**
	 * @param o
	 */
	@Transactional
	public void save(DaylogDay o) {
		o.saveOrUpdate();
	}

	/**
	 * @param sDateNow
	 * @param sDateTagestext
	 * @return
	 */
	public DaylogDay getDateAndCrateIfDateStringNotExists(String sDate, String sDateTagestext) {
		DaylogDay d = get(sDate);
		if (d == null) {
			d = new DaylogDay();
			d.date = sDate;
			d.tagestext = sDateTagestext;
		} else {
			// schon existent
			if (!d.tagestext.isBlank()) {
				d.tagestext = sDateTagestext;
			}
		}
		d.saveOrUpdate();
		return d;
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
