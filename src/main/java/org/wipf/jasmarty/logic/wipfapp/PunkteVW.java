package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

@ApplicationScoped
public class PunkteVW {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("PunkteVW");
	private static final String PUNKTE = "punkte";
	private static final String NOCH_SPIELE = "nochSpiele";

	/**
	 * @return
	 */
	public Integer getPunkte() {

		Integer n = wipfConfig.getConfParamInteger(PUNKTE);
		if (n != null) {
			return n;
		}

		return -999;
	}

	/**
	 * @param n
	 */
	public void setPunkte(Integer n) {
		try {
			wipfConfig.setConfParam(PUNKTE, n);
		} catch (Exception e) {
			LOGGER.warn("setPunkte " + e);
		}
	}

	/**
	 * @return
	 */
	public Integer getNochSpielen() {
		Integer n = wipfConfig.getConfParamInteger(NOCH_SPIELE);
		if (n != null) {
			return n;
		}

		return -999;
	}

	/**
	 * @param n
	 */
	public void setNochSpiele(int n) {
		try {
			wipfConfig.setConfParam(NOCH_SPIELE, n);
		} catch (Exception e) {
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
	 * Bis Max. 50
	 */
	public void appendNochSpiel(int n) {
		Integer nNochSp = getNochSpielen();
		if (nNochSp <= 50) {
			setNochSpiele(nNochSp + n);
		}
	}

}
