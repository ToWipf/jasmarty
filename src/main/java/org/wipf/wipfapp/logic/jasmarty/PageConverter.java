package org.wipf.wipfapp.logic.jasmarty;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.LcdPage;

/**
 * @author wipf
 * 
 */
@ApplicationScoped
public class PageConverter {

	private static final Logger LOGGER = Logger.getLogger("jasmarty PageConverter");
	private LcdPage dynPageCache = new LcdPage();

	@Inject
	JaSmartyConnect jaSmartyConnect;

	LcdPage selectedPage;

	/**
	 * @param page
	 */
	public void selectToNewPage(LcdPage page) {
		// TODO Platzhalter ersetzen
		jaSmartyConnect.clearScreen();
		this.selectedPage = page;
	}

	/**
	 * 
	 */
	public void refreshCache() {
		try {
			convertPage(selectedPage);
		} catch (Exception e) {
			LOGGER.warn("refreshCache: ");
			e.printStackTrace();
		}
	}

	/**
	 * @param page
	 */
	public void convertPage(LcdPage page) {

		// Wenn sich die Seite nicht geändet hat
		if (this.dynPageCache.getId() == page.getId()) {
			// ob sich Vaiablen geändet haben

		} else {
			// Seite wurde geändert
			this.dynPageCache = page;
		}

		for (int nLine = 0; nLine < jaSmartyConnect.getHight(); nLine++) {

			String sL1 = varConverter(page.getLine(nLine));
			String sL2 = sL1.substring(0, Math.min(sL1.length(), jaSmartyConnect.getWidth()));
			char[] sLiout = lineOptions(sL2, nLine);

			jaSmartyConnect.writeLineToCache(0, nLine, sLiout);
		}
	}

	/**
	 * @param sLine
	 * @param nLine
	 * @return
	 */
	private char[] lineOptions(String sLine, int nLine) {
		int sMaxWidth = jaSmartyConnect.getWidth();
		if (sLine.length() == sMaxWidth || sLine.length() == 0) {
			return sLine.toCharArray();
		}
		String sOptions = selectedPage.getOptions();
		if (sOptions == null || sOptions.length() != jaSmartyConnect.getHight()) {
			return sLine.toCharArray();
		}

		char nOption = selectedPage.getOptions().charAt(nLine);

		int nZaehler = 0;
		char[] cOut = new char[jaSmartyConnect.getWidth()];
		// Pauschal mit Leerzeichen init
		for (int i = 0; i < cOut.length; i++) {
			cOut[i] = ' ';
		}

		switch (nOption) {
		case '0':
			// rechtsbündig
			return sLine.toCharArray();
		case '1':
			// mittig
			int nSpacesPerSite = (sMaxWidth - sLine.length()) / 2;

			for (char c : sLine.toCharArray()) {
				cOut[nSpacesPerSite + nZaehler] = c;
				nZaehler++;
			}
			return cOut;

		case '2':
			// linksbündig
			int nSpaces = (sMaxWidth - sLine.length());

			for (char c : sLine.toCharArray()) {
				cOut[nSpaces + nZaehler] = c;
				nZaehler++;
			}
			return cOut;

		case '3':
			// weierlauf auf nächste Zeile
		case '4':
			// hat weiterlauf
		case '5':
			// auto scroll line
		case '6':
			// flash line?

		default:
			return ("Fail 1: " + nOption).toCharArray();
		}
	}

	/**
	 * @param sLine
	 * @return
	 */
	private String varConverter(String sLine) {
		// loop -> suche anzhal $

		if (sLine.length() < 3) {
			return sLine;
		}
		Integer nIndexStart = sLine.indexOf("$");
		if (nIndexStart == -1) {
			return sLine;
		}
		Integer nIndexParaStart = sLine.indexOf("(");
		if (nIndexParaStart == -1) {
			return sLine;
		}
		Integer nIndexEnd = sLine.indexOf(")");
		if (nIndexEnd == -1) {
			return sLine;
		}

		String sBefore = sLine.substring(0, nIndexStart);
		String sCommand = sLine.substring(nIndexStart + 1, nIndexParaStart);
		String sParameter = sLine.substring(nIndexParaStart + 1, nIndexEnd);
		String sAfter = sLine.substring(nIndexEnd + 1, sLine.length());

		switch (sCommand) {
		case "time":
			return sBefore + varTime(sParameter) + sAfter;
		case "bar":
			return sBefore + varBar(sParameter) + sAfter;

		default:
			return "Fail 2: " + sCommand; // Suche nach weiteren vorkommen in dieser Zeile
		}
	}

	/**
	 * @return
	 */
	private String varTime(String sPara) {
		SimpleDateFormat time = new SimpleDateFormat(sPara);
		return time.format(new Date());
	}

	/**
	 * @param sPara
	 * @return
	 */
	private String varBar(String sPara) {
		int nVal = Integer.valueOf(sPara.substring(0, sPara.indexOf(',')));
		int nMax = Integer.valueOf(sPara.substring(sPara.indexOf(',') + 1, sPara.lastIndexOf(',')));
		int nWidth = Integer.valueOf(sPara.substring(sPara.lastIndexOf(',') + 1, sPara.length()));

		int nProzent = (nVal * 100 / nMax);
		int nStand = (nWidth * nProzent / 100);
		String sProzent = String.valueOf(nProzent);
		int nKommastelle = sProzent.charAt(sProzent.length() - 1);
		System.out.println("val:" + nVal + " max:" + nMax + " width:" + nWidth + " stand: " + nStand + " pozentKomma "
				+ sProzent + " komma:" + nKommastelle);

		StringBuilder sb = new StringBuilder();

		// Gefüllte Blöcke:
		sb.append(repeat(JaSmartyConnect.BLOCK_3_3, nStand - 1));

		// komma auswerten
		switch (nKommastelle) {
		case '0':
			sb.append(JaSmartyConnect.BLOCK_0_3);
		case '1':
		case '2':
		case '3':
			sb.append(JaSmartyConnect.BLOCK_1_3);
			break;
		case '4':
		case '5':
		case '6':
			sb.append(JaSmartyConnect.BLOCK_2_3);
			break;
		case '7':
		case '8':
		case '9':
			sb.append(JaSmartyConnect.BLOCK_3_3);
			break;
		}

		// Leere Blöcke
		sb.append(repeat(JaSmartyConnect.BLOCK_0_3, nWidth - nStand - 1));

		System.out.println(sb.toString());

		return sb.toString();
	}

	/**
	 * @param c
	 * @param times
	 * @return
	 */
	private String repeat(char c, int times) {
		if (times < 1) {
			return "";
		}
		return new String(new char[times]).replace('\0', c);
	}

}
