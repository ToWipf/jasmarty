package org.wipf.jasmarty.logic.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.datatypes.WipfUser;

import io.quarkus.elytron.security.common.BcryptUtil;

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
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
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
			LOGGER.info("Standarduser admin erstellen");
			lUserFromJasmartyDb.add(getDefaultUser());
		}
		// User für Healthcheck erstellen
		lUserFromJasmartyDb.add(getHealthCheckUser());

		// Alle User einspielen
		for (WipfUser wu : lUserFromJasmartyDb) {
			addOrUpdateUser(wu, true);
		}
	}

	/**
	 * @param user
	 * @param bAuthDb
	 * @throws SQLException
	 */
	public void addOrUpdateUser(WipfUser user, Boolean bAuthDb) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO users (username, password, role, telegramid) VALUES (?,?,?,?)";

		PreparedStatement statement = null;
		if (bAuthDb) {
			statement = sqlLite.getDbAuth().prepareStatement(sUpdate);
		} else {
			statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		}

		statement.setString(1, user.getUsername());
		statement.setString(2, user.getPassword());
		statement.setString(3, user.getRole());
		statement.setInt(4, user.getTelegramId());
		statement.executeUpdate();
	}

	/**
	 * @param sJson
	 * @return
	 * @throws WipfException
	 */
	public void addOrUpdateUser(String sJson) throws WipfException {
		try {
			addOrUpdateUser(new WipfUser().setByJson(sJson), false);
		} catch (SQLException e) {
			LOGGER.warn("addOrUpdateUser " + e);
		}
	}

	/**
	 * @param sUsername
	 */
	public void deleteUser(String sUsername) {
		try {
			String sUpdate = "DELETE FROM users WHERE username = ?";

			PreparedStatement statement = null;

			statement = sqlLite.getDbApp().prepareStatement(sUpdate);

			statement.setString(1, sUsername);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.warn("deleteUser " + e);
		}
	}

	/**
	 * @param bAuthDb
	 * @return
	 */
	public JSONArray getAllUsersAsJson(Boolean bAuthDb) {
		try {
			LinkedList<WipfUser> lwu = getAllUsers(bAuthDb);
			JSONArray ja = new JSONArray();
			for (WipfUser wu : lwu) {
				ja.put(wu.toJson());
			}
			return ja;

		} catch (SQLException e) {
			LOGGER.warn("getAllUserAsJson " + e);
			return null;
		}

	}

	/**
	 * true = AuthDB
	 * 
	 * false = jasmartyDB
	 * 
	 * @param bAuthDb
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
			rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
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
	 * @return
	 */
	private WipfUser getDefaultUser() {
		WipfUser wu = new WipfUser();
		wu.setUsername("admin");
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.setPassword(BcryptUtil.bcryptHash("jadmin"));
		// wu.setPassword("jadmin");
		wu.setRole("admin");
		wu.setTelegramId(0);
		return wu;
	}

	/**
	 * @return
	 */
	private WipfUser getHealthCheckUser() {
		WipfUser wu = new WipfUser();
		wu.setUsername("check");
		// Mit bcrypt Verschluesselung (slow bei 32Bit)
		wu.setPassword(BcryptUtil.bcryptHash("check"));
		// wu.setPassword("check");
		wu.setRole("check");
		wu.setTelegramId(0);
		return wu;
	}

}
