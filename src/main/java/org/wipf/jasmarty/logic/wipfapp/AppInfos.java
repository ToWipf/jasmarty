package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;

@ApplicationScoped
public class AppInfos {

	@Inject
	PunkteVW punkteVW;
	@Inject
	Wipf wipf;

	/**
	 * @return
	 */
	public String genStarttext() {
		return "Punkte: <strong>" + punkteVW.getPunkte() + "</strong>";
	}

	/**
	 * @return
	 */
	public String genInfotext() {
		return "Wipfapp info Text<br> Zufallszahl: " + wipf.getRandomInt(60);
	}
}
