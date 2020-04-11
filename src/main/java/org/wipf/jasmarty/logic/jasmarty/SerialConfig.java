package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
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
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS config (key TEXT, val TEXT);");
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
			ResultSet rs = stmt.executeQuery("SELECT * FROM config;");

			conf.setPort(rs.getString("port"));
			conf.setRefreshRate(rs.getInt("refreshrate"));
			conf.setWidth(rs.getInt("widht"));
			conf.setHeight(rs.getInt("height"));
			conf.setBaudRate(rs.getInt("baudrate"));
			return conf;
		} catch (Exception e) {
			LOGGER.warn("Config not found");
			return null;
		}
	}

	public boolean setConfig(LcdConfig conf) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT INTO config (key, val) VALUES ('port','" + conf.getPort() + "')");
			stmt.execute("INSERT INTO config (key, val) VALUES ('refreshrate','" + conf.getRefreshRate() + "')");
			stmt.execute("INSERT INTO config (key, val) VALUES ('widht','" + conf.getWidth() + "')");
			stmt.execute("INSERT INTO config (key, val) VALUES ('height','" + conf.getHeight() + "')");
			stmt.execute("INSERT INTO config (key, val) VALUES ('baudrate','" + conf.getBaudRate() + "')");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setConfig(String jnRoot) {
		return setConfig(new LcdConfig().setByJson(jnRoot));
	}

}
