package org.wipf.jasmarty.logic.wipfapp;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class Infotext {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;

	private static final Logger LOGGER = Logger.getLogger("Infotext");
	private static final String APPINFOTEXT = "appinfotext";

	/**
	 * @return
	 */
	public String getText() {
		try {
			return wipfConfig.getConfParamString(APPINFOTEXT);
		} catch (SQLException e) {
			LOGGER.warn("getText " + e);
			return "fail";
		}
	}

	/**
	 * @param n
	 */
	public void setText(String s) {
		try {
			wipfConfig.setConfParam(APPINFOTEXT, s);
		} catch (SQLException e) {
			LOGGER.warn("setText " + e);
		}
	}

}
