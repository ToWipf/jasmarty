package org.wipf.jasmarty.logic.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONObject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Auth {

	@Inject
	Wipf wipf;
	@Inject
	BaseSettings baseSettings;

	private static final Logger LOGGER = Logger.getLogger("Auth");

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS auth (username TEXT UNIQUE, password TEXT, token TEXT);");
		stmt.close();

		if (baseSettings.getAppWorkId() == null) {
			baseSettings.setAppWorkId(wipf.getRandomUUID());
		}
	}

	/**
	 * @param sPasswort
	 * @throws SQLException
	 */
	private boolean createNewUser(String sUsername, String sPasswort, String sAppWorkId) {
		if (sAppWorkId.equals(baseSettings.getAppWorkId())) {

			try {
				Statement stmt = SqlLite.getDB();
				stmt.execute("INSERT OR REPLACE INTO auth (username, password) VALUES ('" + sUsername + "','"
						+ wipf.encrypt(sPasswort, baseSettings.getAppWorkId()) + "')");
				stmt.close();
				LOGGER.info("createNew User: " + sUsername);
				return true;
			} catch (SQLException e) {
				LOGGER.warn("createNew User: " + e);
				return false;
			}
		}
		return false;
	}

	/**
	 * @param sEingabe
	 * @return
	 */
	private String checkAndGenToken(String sUsername, String sEingabe) {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT password FROM auth WHERE username = '" + sUsername + "';");
			String sPwCr = (rs.getString("password"));
			stmt.close();

			if (sPwCr.equals(wipf.encrypt(sEingabe, baseSettings.getAppWorkId()))) {
				// Passwort richtig
				String sNowToken = wipf.getRandomUUID();

				Statement stmtUUID = SqlLite.getDB();
				// TODO nicht das Passwort erneut schreiben
				stmtUUID.execute("INSERT OR REPLACE INTO auth (username, password, token) VALUES ('" + sUsername + "','"
						+ sPwCr + "','" + sNowToken + "')");
				stmtUUID.close();
				return sNowToken;
			}
		} catch (Exception e) {
			LOGGER.warn("check failed: " + e);
		}
		return "FAIL";
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public String check(String jnRoot) {
		JSONObject jo = new JSONObject(jnRoot);
		return checkAndGenToken(jo.getString("username"), jo.getString("password"));
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public boolean createNew(String jnRoot) {
		JSONObject jo = new JSONObject(jnRoot);
		return createNewUser(jo.getString("username"), jo.getString("password"), jo.getString("appWorkId"));
	}

}
