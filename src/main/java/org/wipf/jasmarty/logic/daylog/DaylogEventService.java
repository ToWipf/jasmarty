package org.wipf.jasmarty.logic.daylog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.databasetypes.daylog.DaylogType;
import org.wipf.jasmarty.datatypes.daylog.DaylogStatsTable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@RequestScoped
public class DaylogEventService {

	@Inject
	DaylogTypeService dts;

	/**
	 * @param o @
	 */
	@Transactional
	public void save(DaylogEvent o) {
		o.saveOrUpdate();
	}

	/**
	 * @param nId
	 * @return
	 */
	@Transactional
	public List<DaylogEvent> getByDateId(Integer nId) {
		// KI Generiert!
		// Nach Type Prio sortieren
		List<DaylogType> types = dts.getAll();
		List<DaylogEvent> resultListe = DaylogEvent.findByDateId(nId).list();

		// Erstelle eine Map, um schnell auf die DaylogType-Objekte zuzugreifen
		Map<String, DaylogType> typeMap = types.stream().collect(Collectors.toMap(type -> type.id.toString(), type -> type));

		// Sortiere die resultListe nach der Prio des zugehörigen DaylogType
		resultListe.sort(Comparator.comparingInt(event -> {
			DaylogType type = typeMap.get(event.typid);
			return type != null && type.prio != null ? type.prio : Integer.MAX_VALUE;
		}));
		return resultListe;
	}

	/**
	 * @param nAnzahl
	 * @return
	 */
	public LinkedHashSet<String> getLastByTypeId(String sTypId, Integer nAnzahl) {
		LinkedHashSet<String> o = new LinkedHashSet<>();
		int count = 0;

		for (DaylogEvent d : DaylogEvent.findLastByTypeId(sTypId).list()) {
			if (!o.contains(d.text)) {
				o.add(d.text);
				count++;
				if (count >= nAnzahl) {
					return o;
				}
			}
		}
		return o;
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
	 * @return
	 */
	public List<DaylogEvent> getAll() {
		return DaylogEvent.findAll().list();
	}

	/**
	 * @param sTypIds = "1,2,3,9";
	 * @return
	 */
	@Transactional
	public List<DaylogStatsTable> getStats(String sTypIds) {
		return DaylogEvent.getStatsByTypids(Arrays.asList(sTypIds.split(","))).project(DaylogStatsTable.class).list();
	}

	/**
	 * @param sSearch
	 * @param sType
	 * @return
	 */
	public List<String> getTextBySearchAndType(String sSearch, String sType) {
		LinkedHashSet<String> o = new LinkedHashSet<>();

		// Um doppelte Eintrage zu filtern
		for (DaylogEvent d : DaylogEvent.findByTypeANDText(sType, sSearch).list()) {
			o.add(d.text);
		}

		// Sortieren nach länge
		List<String> l = new ArrayList<String>(o);
		l.sort(Comparator.comparingInt(String::length));
		l.removeIf(s -> s.startsWith("^"));

		return l;
	}

}
