package org.wipf.wipfapp.logic.jasmarty;

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
		// TODO replace date, options ...
		for (int nLine = 0; nLine < jaSmartyConnect.getHight(); nLine++) {

			jaSmartyConnect.writeLineToCache(0, nLine, page.getLine(nLine, jaSmartyConnect.getWidth()));
		}
	}

}
