package org.wipf.jasmarty.logic.daylog;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.wipf.jasmarty.databasetypes.daylog.DaylogType;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@RequestScoped
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
		List<DaylogType> values = DaylogType.<DaylogType>listAll();

		// Wenn eine der Prios null ist -> Nicht sortieren
		for (DaylogType dt : values) {
			if (dt.prio == null) {
				return values;
			}
		}

		// Alle Prios haben einen Wert -> Sortieren
		return DaylogType.<DaylogType>listAll().stream().sorted(Comparator.comparing(DaylogType::getPrio))
				.collect(Collectors.toList());

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
