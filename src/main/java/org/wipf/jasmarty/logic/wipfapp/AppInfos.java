package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Metered;
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
		return "Wipfapp info Text<br> Zufallszahl: " + wipf.getRandomInt(60);
	}
}
