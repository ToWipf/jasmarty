package org.wipf.jasmarty.logic.base;

import java.sql.Connection;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SqlLite {

	@Inject
	AgroalDataSource sqliteDbAuth;

	@Inject
	@DataSource("jasmarty")
	AgroalDataSource sqliteDbJasmarty;

	private static Connection cjasmarty = null;
	private static Connection cauth = null;

	/**
	 * @return
	 * @throws SQLException
	 */
	public Connection getDbJasmarty() throws SQLException {
		if (cjasmarty == null) {
			cjasmarty = sqliteDbJasmarty.getConnection();
		}
		return cjasmarty;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public Connection getDbAuth() throws SQLException {
		if (cauth == null) {
			cauth = sqliteDbAuth.getConnection();
		}
		return cauth;
	}
}
