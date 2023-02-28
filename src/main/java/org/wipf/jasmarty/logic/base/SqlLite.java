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
//	@DataSource("jasmarty")
	AgroalDataSource sqliteDbJasmarty;

	private static Connection cjasmarty = null;

	/**
	 * @return
	 * @throws SQLException
	 */
	public Connection getDbApp() throws SQLException {
		// if (cjasmarty == null) {
		// cjasmarty = sqliteDbJasmarty.getConnection();
		// }
		return cjasmarty;
	}

}
