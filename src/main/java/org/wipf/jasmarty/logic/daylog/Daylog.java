package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Daylog {

	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogTextEventDB daylogTextEventDB;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		daylogDayDB.initDB();
		daylogTextEventDB.initDB();
	}
}
