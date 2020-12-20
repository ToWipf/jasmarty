package org.wipf.jasmarty.logic.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.WipfUser;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class WipfUserVW {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("WipfUserVW");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS users (username TEXT UNIQUE, password TEXT, role TEXT, telegramid INTEGER);";
		sqlLite.getDbAuth().prepareStatement(sUpdate).executeUpdate();
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
		syncUsers();
	}

	/**
	 * Users werden in einer eigenen DB geschrieben, um der jasmarty.db nicht in die
	 * quere zu kommen
	 * 
	 * auth.db ist nicht persistent und wird bei jeden start neu erstellt
	 * 
	 * Users werden daher aus der jasmarty.db in die auth.db geschrieben
	 * 
	 * @throws SQLException
	 */
	public void syncUsers() throws SQLException {
		// get users aus jasmarty
		LinkedList<WipfUser> lUserFromJasmartyDb = getAllUsers(false);

		LOGGER.info("Lade " + lUserFromJasmartyDb.size() + " User");

		if (lUserFromJasmartyDb.size() == 0) {
			// Admin User nur erstellen wenn es kein anderen User gibt
			createDefaultUserAuthDb();
		} else {
			// Alle User einspielen
			for (WipfUser wu : lUserFromJasmartyDb) {
				addOrUpdateUser(wu, true);
			}
		}
	}

	/**
	 * @param user
	 * @throws SQLException
	 */
	public void addOrUpdateUser(WipfUser user, Boolean bAuthDb) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO users (username, password, role, telegramid) VALUES (?,?,?,?)";

		PreparedStatement statement = null;
		if (bAuthDb) {
			statement = sqlLite.getDbAuth().prepareStatement(sUpdate);
		} else {
			statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		}

		statement.setString(1, user.getUsername());
		statement.setString(2, user.getPassword());
		statement.setString(3, user.getRole());
		statement.setInt(4, user.getTelegramId());
		statement.executeUpdate();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public LinkedList<WipfUser> getAllUsers(Boolean bAuthDb) throws SQLException {
		LinkedList<WipfUser> users = new LinkedList<>();

		String sQuery = "SELECT * FROM users";
		ResultSet rs = null;
		if (bAuthDb) {
			rs = sqlLite.getDbAuth().prepareStatement(sQuery).executeQuery();
		} else {
			rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();
		}

		while (rs.next()) {
			WipfUser wu = new WipfUser();
			wu.setUsername(rs.getString("username"));
			wu.setPassword(rs.getString("password"));
			wu.setRole(rs.getString("role"));
			wu.setTelegramId(rs.getInt("telegramid"));
			users.add(wu);
		}
		return users;
	}

	/**
	 * @throws SQLException
	 * 
	 */
	private void createDefaultUserAuthDb() throws SQLException {
		WipfUser wu = new WipfUser();
		wu.setUsername("admin");
		wu.setPassword("jadmin");
		wu.setRole("admin");
		wu.setTelegramId(0);
		addOrUpdateUser(wu, true);
		LOGGER.info("Standarduser admin erstellt");
	}
}
