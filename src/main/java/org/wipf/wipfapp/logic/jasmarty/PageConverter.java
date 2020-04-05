package org.wipf.wipfapp.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.wipfapp.datatypes.LcdPage;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class PageConverter {

	@Inject
	JaSmartyConnect jaSmartyConnect;

	/**
	 * @param page
	 */
	public void convertPage(LcdPage page) {
		// TODO Platzhalter ersetzen
		for (int nLine = 0; nLine < jaSmartyConnect.getHight(); nLine++) {

			// System.out.print("line " + nLine + ": ");
			// System.out.println(page.getLine(nLine, jaSmartyConnect.getWidth()));

			jaSmartyConnect.writeLineToCache(0, nLine, page.getLine(nLine, jaSmartyConnect.getWidth()));
		}
	}

}
