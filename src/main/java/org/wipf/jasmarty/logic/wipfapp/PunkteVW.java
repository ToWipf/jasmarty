package org.wipf.jasmarty.logic.wipfapp;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.jboss.logging.Logger;
import org.json.JSONObject;
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
	private static final String NOCH_SPIELE = "nochSpiele";

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
			wipfConfig.setConfParam(NOCH_SPIELE, n);
		} catch (SQLException e) {
			LOGGER.warn("setPunkte " + e);
		}
	}

	/**
	 * @return
	 */
	public Integer getNochSpiele() {
		try {
			return wipfConfig.getConfParamInteger(NOCH_SPIELE);
		} catch (SQLException e) {
			LOGGER.warn("getNochSpiele " + e);
			return -1;
		}
	}

	/**
	 * @param n
	 */
	public void setNochSpiele(int n) {
		try {
			wipfConfig.setConfParam(PUNKTE, n);
		} catch (SQLException e) {
			LOGGER.warn("setNochSpiele " + e);
		}
	}

	/**
	 * @return
	 */
	@Gauge(name = "punke", unit = MetricUnits.NONE, description = "die Punkte")
	public Integer getPunkteMetric() {
		return getPunkte();
	}

	/**
	 * 
	 */
	public void pluspunkt() {
		setPunkte(getPunkte() + 1);
	}

	/**
	 * 
	 */
	public void minuspunkt() {
		setPunkte(getPunkte() - 1);
	}

	/**
	 * positiv und negativ
	 */
	public void appendPunkt(int n) {
		setPunkte(getPunkte() + n);
	}

	/**
	 * @param json
	 */
	public void playPunkte(String sJson) {
		System.out.println(sJson);

		JSONObject jo = new JSONObject(sJson);
		Integer nEinsatz = jo.getInt("punkte");
		String sCode = jo.getString("code");

		appendPunkt(doPlayPunkte(nEinsatz, sCode));
	}

	/**
	 * @param nEinsatz
	 * @param sCode
	 * @return
	 */
	public Integer doPlayPunkte(Integer nEinsatz, String sCode) {
		if (nEinsatz < 2) {
			return 0;
		}

		if (nEinsatz > getPunkte()) {
			return 0;
		}

		if (sCode.contains("2")) {
			return 2;
		}

		return -2;
	}
}
