package org.wipf.jasmarty.logic.telegram.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppOthers {

	private static final Logger LOGGER = Logger.getLogger("MOthers");

	/**
	 * @param sWuerfelBis
	 * @param sAnzahlWuerfel
	 * @return
	 */
	public static String zufall(String sWuerfelBis, String sAnzahlWuerfel) {
		try {
			return zufall(Integer.parseInt(sWuerfelBis), Integer.parseInt(sAnzahlWuerfel));
		} catch (Exception e) {
			return "Syntax: 'zufall WürfelBis AnzahlWürfel'\n z.B. rnd 60 10\n rnd 6 5";
		}
	}

	/**
	 * @param nWuerfelBis
	 * @param nAnzahlWuerfel
	 * @return
	 */
	public static String zufall(Integer nWuerfelBis, Integer nAnzahlWuerfel) {
		if (nAnzahlWuerfel > 57 || nWuerfelBis > 10421) {
			return "zu viel";
		}

		Random wuerfel = new Random();
		Integer nZahl;
		Integer nAnzahl = 0;
		Integer nSumme = 0;
		StringBuilder sb = new StringBuilder();

		List<Integer> li = new ArrayList<>();

		for (int i = 1; i <= nAnzahlWuerfel; i++) {
			nZahl = wuerfel.nextInt(nWuerfelBis) + 1;
			nSumme += nZahl;
			li.add(nZahl);
		}
		sb.append("Maximale Zahl: " + nWuerfelBis + "\n");
		sb.append("Anzahl Durchgänge: " + nAnzahlWuerfel + "\n");

		// Cont jede Zahl
		sb.append("\nWie oft wurde was gewürfelt:\n");
		for (int i = 1; i <= nWuerfelBis; i++) {
			int nMerke = 0;
			for (Integer n : li) {
				if (n == i) {
					nMerke++;
				}
			}
			if (nMerke > 0) {
				sb.append("Nr." + i + " " + nMerke + "x\n");
			}
		}

		// Alle ausgeben
		sb.append("\nWas wurde wann gewürfelt:\n");
		for (Integer n : li) {
			sb.append("Wurf " + (nAnzahl + 1) + ": " + n.toString() + "\n");
			nAnzahl++;
		}
		sb.append("\nSumme aller Ergebnisse: " + nSumme + "\n");
		sb.append("Duchschnitt aller Ergebnisse: " + nSumme / nAnzahlWuerfel + "\n");

		return sb.toString();
	}

	/**
	 * @return
	 */
	public static String getWitz() {
//		try {
//			String xml = Unirest.get("http://witze.net/witze.rss?cfg=000000410").asString().getBody();
//			// return URLEncoder.encode(parse(xml), "UTF-8");
//			return URLEncoder.encode(parseWitz(xml), "UTF-8");
//
//		} catch (Exception e) {
//			LOGGER.warn("Witzfehler " + e);
//		}
		return "Fail"; // TODO
	}

	/**
	 * @param xml
	 * @return
	 */
	private static String parseWitz(String xml) {
		return xml.substring(xml.lastIndexOf("<description>") + 13, xml.lastIndexOf("</description>"))
				.replaceAll("&lt;br&gt;", "\n");
	}
}
