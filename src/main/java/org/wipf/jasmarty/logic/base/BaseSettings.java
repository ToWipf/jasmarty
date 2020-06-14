package org.wipf.jasmarty.logic.base;

import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.LcdConfig;

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
			bApp = stmt.executeQuery("SELECT val FROM config WHERE key IS '" + sAppname + "';").getBoolean("val");

		} catch (Exception e) {
			LOGGER.error("isAppActive " + e);
		}
		return bApp;
	}

	/**
	 * @return
	 */
	private LcdConfig defaultConfig() {
		LcdConfig lcDef = new LcdConfig();
		lcDef.setPort("COM10");
		lcDef.setHeight(4);
		lcDef.setWidth(20);
		lcDef.setBaudRate(9600);
		lcDef.setRefreshRate(200);

		if (!setConfig(lcDef)) {
			LOGGER.warn("Config konnte nicht gespeichert werden!");
		}
		return lcDef;
	}

	/**
	 * @param conf
	 * @return
	 */
	public boolean setConfig(LcdConfig conf) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('port','" + conf.getPort() + "')");
			stmt.execute(
					"INSERT OR REPLACE INTO config (key, val) VALUES ('refreshrate','" + conf.getRefreshRate() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('widht','" + conf.getWidth() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('height','" + conf.getHeight() + "')");
			stmt.execute("INSERT OR REPLACE INTO config (key, val) VALUES ('baudrate','" + conf.getBaudRate() + "')");
			LOGGER.info("Config gespeichert");
			return true;
		} catch (Exception e) {
			LOGGER.warn("Config konnte nicht gespeichert werden!");
			return false;
		}
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public boolean setConfig(String jnRoot) {
		return setConfig(new LcdConfig().setByJson(jnRoot));
	}

}
