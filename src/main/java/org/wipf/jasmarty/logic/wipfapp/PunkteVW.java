package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.wipfapp.PunkteGewinn;
import org.wipf.jasmarty.datatypes.wipfapp.PunktePlay;
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			LOGGER.warn("setPunkte " + e);
		}
	}

	/**
	 * @return
	 */
	public Integer getNochSpielen() {
		try {
			return wipfConfig.getConfParamInteger(NOCH_SPIELE);
		} catch (Exception e) {
			LOGGER.warn("getNochSpiele " + e);
			return -1;
		}
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

	/**
	 * @param json
	 */
	public String playPunkte(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		Integer nEinsatz = jo.getInt("punkte");
		String sCode = jo.getString("code");

		if (getNochSpielen() > 0) {
			appendNochSpiel(-1);
			PunkteGewinn pRes = doPlayPunkte(+nEinsatz, sCode);

			if (!(getPunkte() + pRes.getPunkte() < 10)) {
				// Nicht unter 10 kommen lassen
				appendPunkt(pRes.getPunkte());
			}

			return pRes.getsText();
		}
		return "fehler 809";
	}

	/**
	 * @param nEinsatz
	 * @param sCode
	 * @return
	 */
	public PunkteGewinn doPlayPunkte(Integer nEinsatz, String sCode) {
		PunkteGewinn pRes = new PunkteGewinn();

		if (nEinsatz > getPunkte() / 4) {
			// Zu hoher Einsatz
			pRes.setsText("Zu hoher Einsatz, das kostet einen Punkt");
			pRes.setPunkte(-1);
			return pRes;
		}

		/// Spiel beginnen
		Integer nZufallsZahl = wipf.getRandomInt(Integer.MAX_VALUE);
		PunktePlay ppIn = new PunktePlay(sCode);
		PunktePlay ppRand = new PunktePlay(String.valueOf(nZufallsZahl));

		Integer nZahlVon0bis6 = ppIn.vergleiche(ppRand);

		// Gewonnen oder Verloren
		if (nZahlVon0bis6 == 6) {
			pRes.setPunkte(nEinsatz * 6);
			pRes.setsText("Spiel um " + nEinsatz + " Punkte mit Code " + sCode + " und der Zufallszahl " + nZufallsZahl
					+ ". Das sind " + nZahlVon0bis6 + " Treffer!" + "." + "<br> Der Gewinn liegt bei <h1>🐧🐧🐧"
					+ pRes.getPunkte() + "🐧🐧🐧<\\h1>");
		} else if (nZahlVon0bis6 == 5) {
			pRes.setPunkte(nEinsatz * 4);
			pRes.setsText("Spiel um " + nEinsatz + " Punkte mit Code " + sCode + " und der Zufallszahl " + nZufallsZahl
					+ ". Das sind " + nZahlVon0bis6 + " Treffer!" + "." + "<br> Der Gewinn liegt bei 🐧🐧<h2>"
					+ pRes.getPunkte() + "🐧🐧<\\h2>");
		} else if (nZahlVon0bis6 == 4) {
			pRes.setPunkte(nEinsatz * 2);
			pRes.setsText("Spiel um " + nEinsatz + " Punkte mit Code " + sCode + " und der Zufallszahl " + nZufallsZahl
					+ ". Das sind " + nZahlVon0bis6 + " Treffer!" + "." + "<br> Der Gewinn liegt bei <strong>🐧"
					+ pRes.getPunkte() + "🐧<strong>");
		} else if (nZahlVon0bis6 == 3) {
			pRes.setPunkte(-nEinsatz / 2);
			pRes.setsText("Spiel um " + nEinsatz + " Punkte mit Code " + sCode + " und der Zufallszahl " + nZufallsZahl
					+ ". Das sind " + nZahlVon0bis6 + " Treffer" + "." + "Der Einsatz geht zur hälfte zurück."
					+ pRes.getPunkte() + "<h1>");
		} else {
			pRes.setPunkte(-nEinsatz);
			pRes.setsText("Spiel um " + nEinsatz + " Punkte mit Code " + sCode + " und der Zufallszahl " + nZufallsZahl
					+ "." + "<br> Der Verlust liegt bei <strong>" + pRes.getPunkte() + "<strong>");
		}

		LOGGER.info(pRes.getsText());
		return pRes;
	}
}
