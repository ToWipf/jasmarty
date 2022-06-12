package org.wipf.jasmarty.logic.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	 */
	public Map<String, String> getAll() {
		Map<String, String> mapData = new HashMap<String, String>();

		try {
			String sQuery = "SELECT * FROM config;";

			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				mapData.put(rs.getString("key"), rs.getString("val"));
			}
		} catch (SQLException e) {
			LOGGER.warn("getAll " + e);
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * @return
	 */
	public JSONArray getAllAsJson() {
		JSONArray ja = new JSONArray();
		try {
			getAll().forEach((key, val) -> {
				JSONObject o = new JSONObject();
				o.put("key", key);
				o.put("val", val);
				ja.put(o);
			});
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson " + e);
			e.printStackTrace();
		}
		return ja;
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
	public Integer getConfParamInteger(String sConfParam) {
		try {
			String sQuery = "SELECT val FROM config WHERE key IS ?;";

			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			statement.setString(1, sConfParam);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				// Es gibt nur einen oder keinen Eintrag
				Integer sVal = rs.getInt("val");
				return (sVal);
			}
		} catch (Exception e) {
			LOGGER.warn("getConfParamInteger bei: " + sConfParam + " fehler");
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

	/**
	 * @param jnRoot
	 * @return
	 */
	public Boolean saveItem(String jnRoot) {
		JSONObject jo = new JSONObject(jnRoot);

		try {
			setConfParam(jo.getString("key"), jo.getString("val"));
		} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param sKey
	 */
	public void deleteItem(String sKey) {
		try {
			String sUpdate = "DELETE FROM config WHERE key LIKE ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, sKey);
			statement.executeUpdate();
			LOGGER.info("delete " + sKey);

		} catch (Exception e) {
			LOGGER.warn("del " + e);
		}
	}

}
