package org.wipf.jasmarty.logic.base;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.base.AuthKey;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class AuthKeyService {

	public List<String> keycache;

	/**
	 * @return
	 */
	public List<AuthKey> getAll() {
		return AuthKey.findAll().list();
	}

	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		AuthKey.findById(nId).delete();
		loadIntoCache();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void save(AuthKey t) {
		t.saveOrUpdate();
		loadIntoCache();
	}

	/**
	 * 
	 */
	public void loadIntoCache() {
		keycache = new LinkedList<>();
		getAll().forEach(o -> {
			if (o.access) {
				keycache.add(o.key);
			}
		});
	}

	/**
	 * @param sKey
	 * @return
	 */
	public boolean isKeyInCache(String sKey) {
		for (String s : keycache) {
			if (s.equals(sKey)) {
				return true;
			}
		}
		return false;
	}

}
