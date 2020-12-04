package org.wipf.jasmarty.logic.base;

import java.sql.Connection;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SqlLite {

	@Inject
	AgroalDataSource sqliteDb;

	private static Connection c = null;

	/**
	 * @return
	 * @throws SQLException
	 */
	public Connection getNewDb() throws SQLException {
		if (c == null) {
			c = sqliteDb.getConnection();
		}
		return c;
	}

}
