package org.wipf.jasmarty.logic.jasmarty;

import java.sql.Statement;

import javax.enterprise.context.RequestScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.LcdConfig;
import org.wipf.jasmarty.logic.base.MsqlLite;

@RequestScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("SerialConfig");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS config (key TEXT UNIQUE, val TEXT);");
		} catch (Exception e) {
			LOGGER.error("init DB");
		}
	}

	/**
	 * @return
	 */
	public LcdConfig getConfig() {
		try {
			LcdConfig conf = new LcdConfig();
			Statement stmt = MsqlLite.getDB();
			conf.setPort(stmt.executeQuery("SELECT val FROM config WHERE key IS 'port';").getString("val"));
			conf.setRefreshRate(stmt.executeQuery("SELECT val FROM config WHERE key IS 'refreshrate';").getInt("val"));
			conf.setWidth(stmt.executeQuery("SELECT val FROM config WHERE key IS 'widht';").getInt("val"));
			conf.setHeight(stmt.executeQuery("SELECT val FROM config WHERE key IS 'height';").getInt("val"));
			conf.setBaudRate(stmt.executeQuery("SELECT val FROM config WHERE key IS 'baudrate';").getInt("val"));
			return conf;
		} catch (Exception e) {
			LOGGER.warn("Config nicht gefunden > default Config erstellen");
			return defaultConfig();
		}
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
			Statement stmt = MsqlLite.getDB();
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
