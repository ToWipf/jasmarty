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
public class WipfConfig {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("WipfConfig");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS config (key TEXT UNIQUE, val TEXT);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
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
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
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
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
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
		String sId = getConfParamString("appworkid");
		if (sId == null) {
			setConfParam("appworkid", UUID.randomUUID().toString());
			// id erneut lesen
			sId = getConfParamString("appworkid");
		}
		LOGGER.info("App ID: " + sId);
		return sId;
	}

	/**
	 * @param sConfParam
	 * @return
	 * @throws SQLException
	 */
	public String getConfParamString(String sConfParam) throws SQLException {
		String sQuery = "SELECT val FROM config WHERE key IS ?;";

		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setString(1, sConfParam);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			// Es gibt nur einen oder keinen Eintrag
			String sVal = rs.getString("val");
			return (sVal);
		}
		return null;
	}

	/**
	 * @param sConfParam
	 * @return
	 * @throws SQLException
	 */
	public Integer getConfParamInteger(String sConfParam) throws SQLException {
		String sQuery = "SELECT val FROM config WHERE key IS ?;";

		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setString(1, sConfParam);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			// Es gibt nur einen oder keinen Eintrag
			Integer sVal = rs.getInt("val");
			return (sVal);
		}
		return null;
	}

	/**
	 * @param sConfParam
	 * @param sVal
	 * @throws SQLException
	 */
	public void setConfParam(String sConfParam, String sVal) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO config (key, val) VALUES (?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setString(1, sConfParam);
		statement.setString(2, sVal);
		statement.executeUpdate();
	}

	/**
	 * @param sConfParam
	 * @param nVal
	 * @throws SQLException
	 */
	public void setConfParam(String sConfParam, Integer nVal) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO config (key, val) VALUES (?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setString(1, sConfParam);
		statement.setInt(2, nVal);
		statement.executeUpdate();
	}

}
