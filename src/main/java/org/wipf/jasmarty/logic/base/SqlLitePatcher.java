package org.wipf.jasmarty.logic.base;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ApplicationScoped
public class SqlLitePatcher {

	@Inject
	WipfConfig wipfConfig;
	@Inject
	SqlLite sqlLite;

	public static final Integer DB_PATCH_VERSION = 4;
	private static final Logger LOGGER = Logger.getLogger("_Patcher__");

	/**
	 * @throws SQLException
	 */
	public void doPatch() throws SQLException {
		Integer nLastVersion = wipfConfig.getConfParamInteger("lastversion");
		// LOGGER.warn("TODO: Aufräumen von Bool und Number Tabellen");

		try {
			if (nLastVersion == null || nLastVersion < DB_PATCH_VERSION) {
				// LOGGER.warn("ALTER TABLE teleUsercache ADD counter INTEGER;");
				// String sUpdate = "ALTER TABLE teleUsercache ADD counter INTEGER;";
				// sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();

//				// PATCH
//				sqlLite.getDbApp().prepareStatement(
//						"CREATE TABLE medien (id INTEGER primary key autoincrement UNIQUE, titel TEXT, art TEXT, typ TEXT, gesehendate INTEGER, infotext TEXT, bewertung INTEGER, editby TEXT, date INTEGER);")
//						.executeUpdate();
				//
//				sqlLite.getDbApp().prepareStatement(
//						"INSERT INTO medien(id, titel, art, gesehendate, infotext, bewertung, editby, date) SELECT id, titel, art, gesehendate, infotext, bewertung, editby, date FROM filme;")
//						.executeUpdate();
			}
		} catch (Exception e) {
			LOGGER.warn("Patch error: " + e);
		}

		setNewPatchVersion();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	private void setNewPatchVersion() throws SQLException {
		LOGGER.info("DB Patch Version:" + DB_PATCH_VERSION);
		wipfConfig.setConfParam("lastversion", DB_PATCH_VERSION);
	}

}
