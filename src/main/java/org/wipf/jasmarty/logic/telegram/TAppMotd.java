package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMotd {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Motd");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS telemotd (id integer primary key autoincrement, text TEXT, editby TEXT, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String delMotd(Telegram t) {
		try {
			String sUpdate = "DELETE FROM telemotd WHERE id = ?";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, t.getMessageIntPart(1));

			statement.executeUpdate();
			return "DEL";
		} catch (Exception e) {
			return "fehler delMotd";
		}
	}

	/**
	 * @param t
	 * @throws SQLException
	 */
	public String addMotd(Telegram t) {
		try {
			String sUpdate = "INSERT OR REPLACE INTO telemotd (text, editby, date) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, t.getMessageFullWithoutFirstWord());
			statement.setString(2, t.getFrom());
			statement.setInt(3, t.getDate());
			statement.executeUpdate();
			return "IN";
		} catch (Exception e) {
			return "fehler addMotd";
		}
	}

	/**
	 * @return
	 */
	public String countMotd() {
		try {
			String sQuery = ("SELECT COUNT(*) FROM telemotd;");

			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				return rs.getString("COUNT(*)") + " Motds in der DB";
			}
			return "Kein count motd vorhanden";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler countMotd";
		}
	}

	/**
	 * @return
	 */
	public String getRndMotd() {
		try {
			String sQuery = "select * from telemotd ORDER BY RANDOM() LIMIT 1";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				return rs.getString("text");
			}
			return "Kein motd vorhanden";

		} catch (Exception e) {
			LOGGER.warn("get telemotd " + e);
			return "Fehler motd";
		}
	}

	/**
	 * @param t
	 * @return
	 * 
	 *         TODO del this -> json
	 */
	public String getAllMotd() {
		try {
			StringBuilder sb = new StringBuilder();
			String sQuery = "select * from telemotd;";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("text") + "\n");
			}
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

}
