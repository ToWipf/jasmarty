package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

@ApplicationScoped
public class AppInfos {

	@Inject
	PunkteVW punkteVW;
	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;

	private static final String APPINFOTEXT = "appinfotext";

	/**
	 * @return
	 */
	@Metered
	public String genStarttext() {
		return "Punkte: <strong>" + punkteVW.getPunkte() + "</strong><br>Stand von "
				+ wipf.getTime("dd MMMM yyyy - HH:mm:ss");
	}

	/**
	 * @return
	 */
	@Metered
	public String genInfotext() {
		return getText();
	}

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
