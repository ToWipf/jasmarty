package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AppInfos {

	@Inject
	PunkteVW punkteVW;

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
		return "Wipfapp info text ! <br> Test?";
	}
}
