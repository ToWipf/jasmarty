package org.wipf.jasmarty.logic.telegram;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppOthers {

	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram Others");

	/**
	 * @param sWuerfelBis
	 * @param sAnzahlWuerfel
	 * @return
	 */
	public String zufall(String sWuerfelBis, String sAnzahlWuerfel) {
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
	public String zufall(Integer nWuerfelBis, Integer nAnzahlWuerfel) {
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
	public String getWitz() {
		try {
			String sWitz;
			sWitz = wipf.httpRequest(Wipf.httpRequestType.GET, "https://funny4you.at/webmasterprogramm/zufallswitz.js.php");
			String s = sWitz.substring(41, sWitz.length() - 3);

			if (s.length() > 5000) {
				return ("Witz zu lang");
			}
			// @formatter:off
			String x = URLEncoder.encode(s, "UTF-8")
					.replaceAll("\\<.*?>", "")
					.replace("+", " ")
					.replace("%22", "\n")
					.replace("%28", "(")
					.replace("%29", ")")
					.replace("%21", "!")
					.replace("%3F", "? ")
					.replace("%3A", ": ")
					.replace("%2C", ",")
					.replace("%2E", ". ")
					.replace("%E2%80%93", ",")
					.replace("%C3%A2%E2%82%AC%CB%9C", "")
					.replace("%26%238242%3B%21", "\"")
					.replace("%26quot%3B", "")
					.replace("%26%2339%3B", "")
					.replace("%26%238242%3B", "")
					.replace("%C3%A2%E2%82%AC%C5%BE", "")
					.replace("%C3%83%E2%80%9E", "Ä")
					.replace("%26Auml%3B", "Ä")
					.replace("%C3%A4", "ä")
					.replace("%C3%83%C2%A4", "ä")
					.replace("%26auml%3B", "ä")
					.replace("%C3%83%E2%80%93", "Ö")
					.replace("H%C3%83%C2%B6", "Ö")
					.replace("%C3%83%C2%B6", "ö")
					.replace("%26ouml%3B", "ö")
					.replace("%C3%B6", "ö")
					.replace("%26Uuml%3B", "Ü")
					.replace("%26uuml%3B", "ü")
					.replace("%3Chr", " ")
					.replace("%3Cbr %2F%3E", " ")
					.replace("%2F%3E%3Csm%3E", "")
					.replace("%C3%BC", "ü")
					.replace("%C3%83%C2%BC", "ü")
					.replace("%C3%83%C5%B8", "ß")
					.replace("%26szlig%3B", "ß")
					.replace("%C3%9F", "ß");
			// @formatter:on
			return x.substring(0, x.indexOf("Ein Witz von"));

		} catch (Exception e) {
			LOGGER.warn("getWitz: " + e);
			return "no Witz";
		}
	}

	/**
	 * Debug Text n Zeilen
	 * 
	 * @param n
	 */
	public String langerText(Integer n) {
		if (n == null) {
			return "langerText <ANZAHL>";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 1; i < n + 1; i++) {
			sb.append("Nummer: " + i + "\n");
		}
		return sb.toString();
	}

}
