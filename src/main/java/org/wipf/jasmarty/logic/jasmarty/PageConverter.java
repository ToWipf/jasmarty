package org.wipf.jasmarty.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.LcdPage;
import org.wipf.jasmarty.logic.base.QMain;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.extensions.Winamp;

/**
 * @author wipf
 * 
 *         TODO scroll
 * 
 */
@ApplicationScoped
public class PageConverter {

	public static char BLOCK_0_3 = '_';
	public static char BLOCK_1_3 = 0x02;
	public static char BLOCK_2_3 = 0x03;
	public static char BLOCK_3_3 = 0xFF;

	private LcdPage selectedPage;

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Wipf wipf;
	@Inject
	Winamp winamp;

	/**
	 * @param page
	 * @throws Exception
	 */
	public void selectToNewPage(LcdPage page) throws Exception {
		// TODO cls noch nötig?
		lcdConnect.clearScreen();
		this.selectedPage = page;
		// Manuellen Refresh des caches anstoßen
		this.refreshCache();
	}

	/**
	 * @return
	 */
	public LcdPage getCurrentSite() {
		return selectedPage;
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void refreshCache() throws Exception {
		convertPage(selectedPage);
	}

	/**
	 * @param page
	 * @throws Exception
	 */
	private void convertPage(LcdPage page) throws Exception {
		for (int nLine = 0; nLine < lcdConnect.getHeight(); nLine++) {
			String sLineAfterConvert = searchAndReplaceVars(page.getLine(nLine));

			// Zeile in Cache schreiben und auf maximale Zeichenanzahl kürzen
			lcdConnect.writeLineToCache(0, nLine, lineOptions(sLineAfterConvert, nLine));
		}
	}

	/**
	 * Die Optionen der entsprechenden Zeilen beachten z.B. mittig, linksbündig,
	 * rechtsbündig
	 * 
	 * @param sLine
	 * @param nLine
	 * @return
	 */
	private char[] lineOptions(String sLine, int nLine) {
		String sOptions = selectedPage.getOptions();

		// Wenn die Zeile genau voll oder leer ist -> nichts machen
		// Wenn keine Options vorhanden sind -> nichts machen
		if (sLine.length() == 0 || sLine.length() == lcdConnect.getWidth() || sOptions == null) {
			return sLine.toCharArray();
		}

		// Zeilenoption bestimmen
		char nOption = selectedPage.getOptions().charAt(nLine);

		switch (nOption) {
		case '0':
			// rechtsbündig
			return lineRechts(sLine);
		case '1':
			// mittig
			return lineMittig(sLine);
		case '2':
			// linksbündig
			return lineLinks(sLine);
		case '3':
			// weierlauf auf nächste Zeile
			return "Fehler 13".toCharArray();
		case '4':
			// hat weiterlauf
			return "Fehler 14".toCharArray();
		case '5':
			// Scroll
			return scrollLine(sLine, nLine);
		case '6':
			// flash line?
			return "Fehler 16".toCharArray();

		default:
			return ("Fail 1: " + nOption).toCharArray();
		}
	}

	/**
	 * @param sLine
	 * @param nLine
	 * @return
	 */
	private char[] scrollLine(String sLine, int nLine) {
		int nLength = sLine.length();
		if (nLength > lcdConnect.getWidth()) {
			// char[] caIst = lcdConnect.getCache().getCacheSollLine(nLine);
			int nState = lcdConnect.getCache().getScrollStateForLine(nLine);
			lcdConnect.getCache().setScrollStateForLine(nLine, nState + 1);

			if (nState > nLength) {
				lcdConnect.getCache().setScrollStateForLine(nLine, 2);
			}

			if (nLength - nState < lcdConnect.getWidth()) {
				// Zeile erneut hinten anhängen
				sLine = sLine + sLine;
			}

			return lineRechts(sLine.substring(nState));
		} else {
			// Nicht nötig zu scrollen -> Rechtsbündig
			// TODO ineffizient
			return lineRechts(sLine);
		}
	}

	/**
	 * @param sLine
	 * @return
	 */
	private char[] lineRechts(String sLine) {
		// sLine = sLine.toString() + wipf.repeat(' ', lcdConnect.getWidth() -
		// sLine.length());
		// return sLine.substring(0, Math.min(sLine.length(),
		// lcdConnect.getWidth())).toCharArray();

		// Den rest mit Leerzeichen fuellen -> Lösche alte zeichen
		return (sLine.toString() + wipf.repeat(' ', lcdConnect.getWidth() - sLine.length())).toCharArray();
	}

	/**
	 * @param sLine
	 * @return
	 */
	private char[] lineLinks(String sLine) {
		// Ausgabezeile vorbereiten -> Pauschal mit Leerzeichen init
		char[] cOut = new char[lcdConnect.getWidth()];
		for (int i = 0; i < cOut.length; i++) {
			cOut[i] = ' ';
		}
		int nZaehler = 0;
		int nSpaces = (lcdConnect.getWidth() - sLine.length());

		for (char c : sLine.toCharArray()) {
			cOut[nSpaces + nZaehler] = c;
			nZaehler++;
		}
		return cOut;
	}

	/**
	 * @param sLine
	 * @return
	 */
	private char[] lineMittig(String sLine) {
		// Ausgabezeile vorbereiten -> Pauschal mit Leerzeichen init
		char[] cOut = new char[lcdConnect.getWidth()];
		for (int i = 0; i < cOut.length; i++) {
			cOut[i] = ' ';
		}
		int nZaehler = 0;
		int nSpacesPerSite = (lcdConnect.getWidth() - sLine.length()) / 2;

		for (char c : sLine.toCharArray()) {
			cOut[nSpacesPerSite + nZaehler] = c;
			nZaehler++;
		}
		return cOut;
	}

	/**
	 * Alle Variablen finden und und ersetzen
	 * 
	 * @param sLine
	 * @return
	 * @throws Exception
	 */
	private String searchAndReplaceVars(String sLine) throws Exception {
		String sOut = sLine;

		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = sLine.indexOf("$", lastIndex);

			if (lastIndex != -1) {
				lastIndex += 1;
				sOut = lineReplaceVars(sOut);
			}
		}
		return sOut;
	}

	/**
	 * Sucht von hinen alle $var() und ersetzt diese
	 * 
	 * @param sLine
	 * @return
	 * @throws Exception
	 */
	private String lineReplaceVars(String sLine) throws Exception {

		// Positionen bestimmen
		if (sLine.length() < 3) {
			return sLine;
		}
		Integer nIndexStart = sLine.lastIndexOf('$');
		if (nIndexStart == -1) {
			return sLine;
		}
		Integer nIndexParaStart = sLine.lastIndexOf('(');
		if (nIndexParaStart == -1) {
			return sLine;
		}
		Integer nIndexEnd = sLine.indexOf(')', nIndexParaStart);
		if (nIndexEnd == -1) {
			return sLine;
		}

		// Werte ermittelen
		String sBefore = sLine.substring(0, nIndexStart);
		String sCommand = sLine.substring(nIndexStart + 1, nIndexParaStart);
		String sParameter = sLine.substring(nIndexParaStart + 1, nIndexEnd);
		String sAfter = sLine.substring(nIndexEnd + 1, sLine.length());

		switch (sCommand) {
		case "time":
			return sBefore + wipf.time(sParameter) + sAfter;
		case "bar":
			return sBefore + varBar(sParameter) + sAfter;
		case "math":
			return sBefore + wipf.doMathByString(sParameter) + sAfter;
		case "rnd":
			return sBefore + wipf.getRandomInt(sParameter) + sAfter;
		case "winamp":
			return sBefore + winamp.getInfos(sParameter) + sAfter;
		case "pos":
			return sBefore + wipf.repeat(' ', Integer.valueOf(sParameter) - nIndexStart) + sAfter;
		case "ver":
			return sBefore + QMain.VERSION + sAfter;
		default:
			return "Fail 2: " + sCommand; // Suche nach weiteren vorkommen in dieser Zeile
		}
	}

	/**
	 * @param sPara
	 * @return
	 */
	private String varBar(String sPara) {
		// Parameter kann 'Mathe' haben
		int nVal = Integer.valueOf(sPara.substring(0, sPara.indexOf(',')));
		int nMax = Integer.valueOf(sPara.substring(sPara.indexOf(',') + 1, sPara.lastIndexOf(',')));
		int nWidth = Integer.valueOf(sPara.substring(sPara.lastIndexOf(',') + 1, sPara.length()));

		if (nVal < 1) {
			nVal = 0;
		}

		int nFillBis = (nVal * nWidth * 3 / nMax);

		StringBuilder sb = new StringBuilder();

		// Gefüllte Blöcke:
		sb.append(wipf.repeat(BLOCK_3_3, nFillBis / 3));

		// komma auswerten
		switch (nFillBis % 3) {
		case 0:
			sb.append(BLOCK_0_3);
			break;
		case 1:
			sb.append(BLOCK_1_3);
			break;
		case 2:
			sb.append(BLOCK_2_3);
			break;
		case 3:
			sb.append(BLOCK_3_3);
			break;
		}

		// Leere Blöcke:
		sb.append(wipf.repeat(BLOCK_0_3, (nWidth - (nFillBis / 3)) - 1));
		return sb.toString();
	}

}
