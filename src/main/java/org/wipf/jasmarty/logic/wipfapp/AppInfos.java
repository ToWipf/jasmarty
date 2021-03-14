package org.wipf.jasmarty.logic.wipfapp;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppInfos {

	/**
	 * @return
	 */
	public String genStarttext() {
		return "Wipfapp !<br><strong>Testversion</strong>";
	}

	/**
	 * @return
	 */
	public String genInfotext() {
		return "Wipfapp info text ! <br> Test?";
	}
}
