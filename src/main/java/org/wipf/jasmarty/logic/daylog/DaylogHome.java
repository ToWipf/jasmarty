package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogHome {

	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogEventDB daylogEventDB;
	@Inject
	DaylogTypeDB daylogTypeDB;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		daylogDayDB.initDB();
		daylogEventDB.initDB();
		daylogTypeDB.initDB();
	}

}