package org.wipf.jasmarty.logic.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class BaseSettings {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("BaseSettings");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS config (key TEXT UNIQUE, val TEXT);";
		sqlLite.getNewDb().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param sAppname
	 * @return
	 * @throws SQLException
	 */
	public boolean isAppActive(String sAppname) throws SQLException {
		Boolean bApp = getAppActive(sAppname);
		if (bApp == null) {
			LOGGER.info("App " + sAppname + " ist nicht definert");
			setAppStatus(sAppname, false);
		}

		return getAppActive(sAppname);
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private Boolean getAppActive(String sAppname) throws SQLException {
		String sQuery = "SELECT val FROM config WHERE key IS ?;";
		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sQuery);
		statement.setString(1, "app_" + sAppname);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			// Es gibt nur einen oder keinen Eintrag
			return (rs.getBoolean("val"));
		}
		return null;
	}

	/**
	 * @param conf
	 * @return
	 * @throws SQLException
	 */
	public void setAppStatus(String sAppname, boolean bStatus) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO config (key, val) VALUES (?,?)";
		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sUpdate);
		statement.setString(1, "app_" + sAppname);
		statement.setBoolean(2, bStatus);
		statement.executeUpdate();

		LOGGER.info("Config gespeichert: " + sAppname + " ist jetzt " + bStatus);
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public String checkAppWorkId() throws SQLException {
		String sId = getAppWorkId();
		if (sId == null) {
			setAppWorkId(UUID.randomUUID().toString());
			// id erneut lesen
			sId = getAppWorkId();
		}
		return sId;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public String getAppWorkId() throws SQLException {
		String sQuery = "SELECT val FROM config WHERE key IS 'appworkid';";

		ResultSet rs = sqlLite.getNewDb().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			// Es gibt nur einen oder keinen Eintrag
			String sId = rs.getString("val");
			LOGGER.info("AppWorkId ist: " + sId);
			return (sId);
		}
		return null;
	}

	/**
	 * @param sID
	 * @return
	 * @throws SQLException
	 */
	private void setAppWorkId(String sId) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO config (key, val) VALUES ('appworkid',?)";
		PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sUpdate);
		statement.setString(1, sId);
		statement.executeUpdate();

		LOGGER.info("Config gespeichert: setAppWorkId ist jetzt " + sId);
	}

}
