package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

@ApplicationScoped
public class Infotext {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;

	private static final String APPINFOTEXT = "appinfotext";

	/**
	 * @return
	 */
	public String getText() {
		return wipfConfig.getConfParamString(APPINFOTEXT);
	}

	/**
	 * @param n
	 */
	public void setText(String s) {
		wipfConfig.setConfParam(APPINFOTEXT, s);
	}

}
