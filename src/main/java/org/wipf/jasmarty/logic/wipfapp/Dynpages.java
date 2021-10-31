package org.wipf.jasmarty.logic.wipfapp;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.wipfapp.Dynpage;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
public class Dynpages {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	/**
	 * 
	 */
	public void initDB() {
		try {
			String sUpdate = "CREATE TABLE IF NOT EXISTS dynpage (pageid INTEGER, html TEXT, script TEXT, style TEXT);";
			sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param dPage
	 */
	public void save(Dynpage dPage) {

	}

}
