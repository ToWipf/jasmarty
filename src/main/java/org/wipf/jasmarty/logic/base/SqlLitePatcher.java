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

	private static final Logger LOGGER = Logger.getLogger("Patcher");

	/**
	 * @throws SQLException
	 */
	public void doPatch() throws SQLException {
		Integer nLastVersion = wipfConfig.getConfParamInteger("lastversion");

		try {
			if (nLastVersion == null || nLastVersion < 1) {
				LOGGER.warn("DROP TABLE daylogDay;");
				String sUpdate = "DROP TABLE daylogDay;";
				sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
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
		LOGGER.info("DB Patch Version:" + MainHome.DB_PATCH_VERSION);
		wipfConfig.setConfParam("lastversion", MainHome.DB_PATCH_VERSION);
	}

}
