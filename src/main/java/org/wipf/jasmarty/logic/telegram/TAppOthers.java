package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;

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
			sWitz = wipf.httpRequest(Wipf.httpRequestType.GET,
					"https://funny4you.at/webmasterprogramm/zufallswitz.js.php");
			String s = sWitz.substring(41, sWitz.length() - 3).replaceAll("\\<.*?>", "");
			String x = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", " ").replaceAll("%22", "\n")
					.replaceAll("%28", "(").replaceAll("%29", ")").replaceAll("%21", "!").replaceAll("%3F", "?")
					.replaceAll("%3A", ":").replaceAll("%2C", ",").replaceAll("%2E", ". ")
					.replaceAll("%C3%83%C2%BC", "ü").replaceAll("%C3%83%C2%A4", "ä").replaceAll("%26uuml%3B", "ü")
					.replaceAll("%C3%83%C5%B8", "ß").replaceAll("%26szlig%3B", "ß").replaceAll("%26ouml%3B", "ö")
					.replaceAll("%C3%A2%E2%82%AC%CB%9C", "").replaceAll("%26uuml%3B", "Ü")
					.replaceAll("%26%238242%3B%21", "\"").replaceAll("%26auml%3B", "ä").replaceAll("H%C3%83%C2%B6", "Ö")
					.replaceAll("%26quot%3B", "").replaceAll("%C3%83%C2%B6", "ö").replaceAll("%26%2339%3B", "")
					.replaceAll("%26%238242%3B", "").replaceAll("%C3%A2%E2%82%AC%C5%BE", "")
					.replaceAll("%C3%83%E2%80%93", "Ö").replaceAll("%C3%83%E2%80%9E", "Ä");

			// TODO
			return x.substring(0, x.indexOf("Ein Witz von"));

		} catch (IOException e) {
			LOGGER.warn("getWitz: " + e);
			return "no Witz";
		}
	}

	/**
	 * @return
	 */
	public String getOnline() {
		try {
			return wipf.httpRequest(Wipf.httpRequestType.GET, "http://192.168.2.11:9042");

		} catch (IOException e) {
			LOGGER.warn("getOnline: " + e);
			return "getOnline failed";
		}
	}

	/**
	 * @return
	 */
	public String getTemperature() {
		try {
			return wipf.httpRequest(Wipf.httpRequestType.GET, "http://192.168.2.14:80");

		} catch (IOException e) {
			LOGGER.warn("getTemperature: " + e);
			return "getTemperature failed";
		}
	}

	/**
	 * @return
	 */
	public String getSystem() {
		try {
			return wipf.httpRequest(Wipf.httpRequestType.GET, "http://192.168.2.33:80/metrics");

		} catch (IOException e) {
			LOGGER.warn("getSystem: " + e);
			return "getSystem failed";
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

	/**
	 * @return
	 */
	public String getFileList() {
		// File f = new File(Paths.get("").toAbsolutePath().toString() );
		File f = new File("files/");

		StringBuilder sb = new StringBuilder();
		for (String sL : f.list()) {
			// Grafana Bilder nicht listen
			if (!sL.startsWith("grafana_")) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(sL);
			}
		}

		// Freier Platz in GB
		sb.append("\n\nFrei: " + new File("/").getUsableSpace() / Math.pow(1024, 3) + " GB");

		return sb.toString();
	}
}
