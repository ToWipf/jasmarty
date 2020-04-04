package org.wipf.wipfapp.logic.jasmarty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.wipf.wipfapp.datatypes.LcdPage;

@RequestScoped
public class PageConverter {

	@Inject
	JaSmartyConnect jaSmartyConnect;

	public void selectPage(LcdPage page) {
		// TODO Platzhalter ersetzen
		for (int nLine = 0; nLine < jaSmartyConnect.getHight(); nLine++) {
			jaSmartyConnect.writeLineToCache(0, nLine, page.getLine(nLine));
		}
	}

}
