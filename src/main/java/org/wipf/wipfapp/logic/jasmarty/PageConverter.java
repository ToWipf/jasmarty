package org.wipf.wipfapp.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.LcdPage;

/**
 * @author wipf
 * 
 *         Seite umbauen und Vaiablen anpassen
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
			LOGGER.warn("refreshCache: " + e);
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

		// TODO replace date, options ...
		for (int nLine = 0; nLine < jaSmartyConnect.getHight(); nLine++) {

			char[] sLiout = lineOptions(page.getLine(nLine, jaSmartyConnect.getWidth()), nLine);

			jaSmartyConnect.writeLineToCache(0, nLine, sLiout);
		}
	}

	private char[] lineOptions(String sLine, int nLine) {
		char nOption = selectedPage.getOptions().charAt(nLine);

		int sMaxWidth = jaSmartyConnect.getWidth();
		int nZaehler = 0;
		char[] cOut = new char[jaSmartyConnect.getWidth()];
		// Pauschal mit Leerzeichen init
		for (int i = 0; i < cOut.length; i++) {
			cOut[i] = ' ';
		}

		// if sLine = len 20 -> tue nichts TODO

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

			cOut[nSpacesPerSite] = ' ';
			return cOut;

		case '2':
			// linksbündig

		default:
			return ("Fail 1: " + nOption).toCharArray();
		}
	}

	private void replaceVaiables() {

	}

}
