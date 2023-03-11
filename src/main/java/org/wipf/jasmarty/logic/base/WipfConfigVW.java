package org.wipf.jasmarty.logic.base;


import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.databasetypes.base.WipfConfig;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class WipfConfigVW {

	private static final Logger LOGGER = Logger.getLogger("WipfConfigVW");
	private static String APP_PREFIX = "app_";

	/**
	 * @param sAppname
	 * @return
	 */
	@Transactional
	public Boolean isAppActive(String sAppname) {
		WipfConfig wc = WipfConfig.findByKey(APP_PREFIX + sAppname).firstResult();
		if (wc == null) {
			// Config initial erstellen
			new WipfConfig().setBy(APP_PREFIX + sAppname, "0").persist();
			return false;
		}
		return wc.value.equals("1");
	}

	/**
	 * @return
	 */
	public List<WipfConfig> getAll() {
		return WipfConfig.findAll().list();
	}

	/**
	 * @param sConfParam
	 * @return
	 * 
	 */
	public String getConfParamString(String sConfParam) {
		WipfConfig wc = WipfConfig.findByKey(sConfParam).firstResult();
		if (wc != null) {
			return wc.value;
		}
		return null;
	}

	/**
	 * @param sConfParam
	 * @return
	 * 
	 */
	public Integer getConfParamInteger(String sConfParam) {
		try {
			WipfConfig wc = WipfConfig.findByKey(sConfParam).firstResult();
			if (wc != null) {
				return Integer.valueOf(wc.value);
			}
			return null;
			// TODO
		} catch (Exception e) {
			LOGGER.warn("getConfParamInteger bei: " + sConfParam + " fehler");
		}
		return null;
	}

	/**
	 * @param sConfParam
	 * @param sVal
	 */
	@Transactional
	public void setConfParam(String sConfParam, String sVal) {
		new WipfConfig().setBy(sConfParam, sVal).saveOrUpdate();
	}

	/**
	 * @param sConfParam
	 * @param nVal
	 */
	@Transactional
	public void setConfParam(String sConfParam, Integer nVal) {
		setConfParam(sConfParam, nVal.toString());
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	@Transactional
	public void saveItem(WipfConfig wu) {
		wu.saveOrUpdate();
	}

	/**
	 * @param sKey
	 */
	@Transactional
	// TODO auf id umstellen
	public void deleteItem(String sKey) {
		WipfConfig.findByKey(sKey).firstResultOptional().ifPresent(o -> {
			o.delete();
		});
	}

	/**
	 * 
	 * 
	 */
	public String checkAppWorkId() {
		String sId = getConfParamString("appworkid");
		if (sId == null) {
			setConfParam("appworkid", UUID.randomUUID().toString());
			// id erneut lesen
			sId = getConfParamString("appworkid");
		}
		LOGGER.info("App ID: " + sId);
		return sId;
	}

}
