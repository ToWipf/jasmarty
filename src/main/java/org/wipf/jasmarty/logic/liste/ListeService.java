package org.wipf.jasmarty.logic.liste;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.liste.Liste;
import org.wipf.jasmarty.logic.base.Wipf;

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
	public List<Liste> getAll() throws SQLException {
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
