package org.wipf.jasmarty.logic.listen;

import java.util.List;

import org.wipf.jasmarty.databasetypes.liste.Liste;
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
		List<Liste> res = (Liste.findLast().list());
		if (res.size() < nAnzahl) {
			return res;
		}
		return res.subList(0, nAnzahl);
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

}
