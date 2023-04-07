package org.wipf.jasmarty.logic.daylog;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.datatypes.DaylogStatsTable;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogEventService {

	/**
	 * @param o @
	 */
	@Transactional
	public void save(DaylogEvent o) {
		o.saveOrUpdate();
	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 */
	public List<DaylogEvent> getByDateId(Integer nId) {
		return DaylogEvent.findByDateId(nId).list();
	}

	/**
	 * @param nTypId
	 * @return
	 */
	public List<DaylogEvent> getAllByTypId(String sTypId) {
		return DaylogEvent.findByTypeId(sTypId).list();
	}

	/**
	 * @param sTypIds
	 * @return
	 */
	public List<DaylogEvent> getAllByTypIds(String sTypIds) {
		return DaylogEvent.findByTypeIds(Arrays.asList(sTypIds.split(","))).list();
	}

	/**
	 * @param sDate @
	 */
	@Transactional
	public void del(Integer nId) {
		DaylogEvent.findById(nId).delete();
	}

	/**
	 * @param nUserId
	 * @return
	 */
	public List<DaylogEvent> getAll() {
		return DaylogEvent.findAll().list();
	}

	/**
	 * @param sTypIds = "1,2,3,9";
	 * @return
	 */
	public List<DaylogStatsTable> getStats(String sTypIds) {
		return DaylogEvent.getStatsByTypids(Arrays.asList(sTypIds.split(","))).project(DaylogStatsTable.class).list();
	}

	/**
	 * @param sSearch
	 * @param sType
	 * @return
	 */
	public LinkedHashSet<String> getTextBySearchAndType(String sSearch, String sType) {
		LinkedHashSet<String> o = new LinkedHashSet<>();

		for (DaylogEvent d : DaylogEvent.findByTypeANDText(sType, sSearch).list()) {
			o.add(d.text);
			System.out.println(d.toString());
		}

		return o;
	}

}
