package org.wipf.jasmarty.logic.base;

import java.util.LinkedList;
import java.util.List;


import org.wipf.jasmarty.databasetypes.base.AuthKey;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

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
		loadKeysIntoCache();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void save(AuthKey t) {
		t.saveOrUpdate();
		loadKeysIntoCache();
	}

	/**
	 * 
	 */
	public void loadKeysIntoCache() {
		keycache = new LinkedList<String>();
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

	/**
	 * @param sKey
	 */
	public void newKey(String sKey) {
		AuthKey k = new AuthKey();
		k.access = false;
		k.key = sKey;
		save(k);
	}

}
