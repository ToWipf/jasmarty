package org.wipf.jasmarty.logic.listen;

import java.util.LinkedList;
import java.util.List;

import org.wipf.jasmarty.databasetypes.liste.Liste;
import org.wipf.jasmarty.databasetypes.liste.ListeType;
import org.wipf.jasmarty.logic.base.Wipf;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class ListeService {

	@Inject
	Wipf wipf;
	@Inject
	ListeTypeService listeTypeService;

	/**
	 * @param l
	 */
	@Transactional
	public void save(Liste l) {
		l.saveOrUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		Liste.findById(nId).delete();
	}

	/**
	 * @param t
	 * @return
	 */
	public List<Liste> getAll() {
		return Liste.findAll().list();
	}

	/**
	 * @param nTypeId
	 * @return
	 */
	public List<Liste> getAllByType(Integer nTypeId) {
		return Liste.findByType(nTypeId).list();
	}

	/**
	 * @param nAnzahl
	 * @return
	 */
	public List<Liste> getLast(Integer nAnzahl) {
		List<ListeType> types = listeTypeService.getAll();
		List<Liste> res = Liste.findLast().list();
		List<Liste> out = new LinkedList<>();

		for (Liste item : res) {
			for (ListeType typ : types) {
				if (item.typeid == typ.id && (typ.showOverview == null || typ.showOverview)) {
					out.add(item);
				}
				if (out.size() == nAnzahl) {
					return out;
				}
			}
		}

		return out;
	}

	/**
	 * @param t
	 * @return
	 */
	public String addStringToList(String s) {
		Liste l = new Liste();
		l.data = s;
		l.typeid = 1;
		l.date = wipf.getTime("yyyy-MM-dd");
		save(l);
		return l.toString();
	}

	/**
	 * Fuer HW Button
	 */
	public void saveTime() {
		Liste l = new Liste();
		l.data = "saveTime@" + wipf.getTime("HH:mm:ss");
		l.typeid = 1;
		l.date = wipf.getTime("yyyy-MM-dd");
		save(l);
	}

}
