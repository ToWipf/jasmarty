package org.wipf.jasmarty.logic.wipfapp;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class PunkteVW {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("PunkteVW");
	private static final String PUNKTE = "punkte";

	/**
	 * @return
	 */
	public Integer getPunkte() {
		try {
			return wipfConfig.getConfParamInteger(PUNKTE);
		} catch (SQLException e) {
			LOGGER.warn("getPunkte " + e);
			return -99999;
		}
	}

	/**
	 * @param n
	 */
	public void setPunkte(int n) {
		try {
			wipfConfig.setConfParam(PUNKTE, n);
		} catch (SQLException e) {
			LOGGER.warn("setPunkte " + e);
		}
	}
}
