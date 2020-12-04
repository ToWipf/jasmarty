package org.wipf.jasmarty.logic.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ATEST {

	@Inject
	Wipf wipf;
	@Inject
	BaseSettings baseSettings;
	@Inject
	SqlLite sqlLite;

	public void ntest() {
		try {
			initDB();
			schreiben();
			lesen();
			lesen();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		System.out.println("INIT");

		String sUpdate = "CREATE TABLE IF NOT EXISTS auth (username TEXT UNIQUE, password TEXT, token TEXT);";
		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sUpdate);
		statement.executeUpdate();

	}

	/**
	 * @param sEingabe
	 * @return
	 * @throws SQLException
	 */
	private void schreiben() throws SQLException {
		// SCHREIBEN
		System.out.println("SCHREIBEN");
		String sUpdate = "INSERT OR REPLACE INTO auth (username, password, token) VALUES ('" + "HALLO" + "','"
				+ "123423" + "','" + "asdfjgkljdfalkgjk" + "')";
		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sUpdate);
		statement.executeUpdate();

	}

	/**
	 * @throws SQLException
	 * 
	 */
	private void lesen() throws SQLException {
		// LESEN
		System.out.println("LESEN");
		String sQuery = "SELECT password FROM auth WHERE username = '" + "HALLO" + "';";

		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();
		System.out.println(rs.getString("password"));

	}

}
