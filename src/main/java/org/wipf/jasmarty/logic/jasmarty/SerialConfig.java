package org.wipf.jasmarty.logic.jasmarty;

import java.sql.Statement;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.LcdConfig;
import org.wipf.jasmarty.logic.base.MsqlLite;

@RequestScoped
public class SerialConfig {

	@Inject
	JaSmartyConnect jaSmartyConnect;

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
		return jaSmartyConnect.getConfig();
	}

	public boolean setConfig(LcdConfig conf) {

		return true;
	}

	public String setConfig(String jnRoot) {
		// TODO Auto-generated method stub
		return null;
	}

}
