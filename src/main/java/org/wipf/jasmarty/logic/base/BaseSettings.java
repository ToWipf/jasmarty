package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class BaseSettings {

	private static final Logger LOGGER = Logger.getLogger("BaseSettings");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS config (key TEXT UNIQUE, val TEXT);");
			stmt.close();
		} catch (Exception e) {
			LOGGER.error("init DB " + e);
		}
	}

	/**
	 * @return
	 */
	public boolean isAppActive(String sAppname) {
		boolean bApp = false;
		try {
			Statement stmt = SqlLite.getDB();
			String s = stmt.executeQuery("SELECT val FROM config WHERE key IS '" + "app_" + sAppname + "';")
					.getString("val");
			if (s.equals("true")) {
				bApp = true;
			}
			stmt.close();
		} catch (SQLException e) {
			if (e.getMessage().equals("ResultSet closed")) {
				LOGGER.info("App " + sAppname + " ist nicht definert");
				setAppStatus(sAppname, false);
			} else
				LOGGER.warn("isAppActive " + e);
		}
		return bApp;
	}

	/**
	 * @param conf
	 * @return
	 */
	public boolean setAppStatus(String sAppname, boolean bStatus) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute(
					"INSERT OR REPLACE INTO config (key, val) VALUES ('" + "app_" + sAppname + "','" + bStatus + "')");
			LOGGER.info("Config gespeichert: " + sAppname + " ist jetzt " + bStatus);
			return true;
		} catch (Exception e) {
			LOGGER.warn("setAppStatus " + e);
			return false;
		}
	}

	/**
	 * @param sID
	 * @return
	 */
	public boolean setAppWorkId(String sID) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('" + "appworkid" + "','" + sID + "')");
			LOGGER.info("Config gespeichert: setAppWorkId ist jetzt " + sID);
			return true;
		} catch (Exception e) {
			LOGGER.warn("setAppStatus " + e);
			return false;
		}
	}

	/**
	 * @return
	 */
	public String getAppWorkId() {
		try {
			Statement stmt = SqlLite.getDB();
			String s = stmt.executeQuery("SELECT val FROM config WHERE key IS '" + "appworkid" + "';").getString("val");
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("getAppWorkId " + e);
		}
		return null;
	}

}
