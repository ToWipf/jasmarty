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
public class TAppEssen {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Essen");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS essen (id integer primary key autoincrement, type TEXT, name TEXT, options TEXT, editby TEXT, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String menueEssen(Telegram t) {
		try {
			String sAction = t.getMessageStringPartLow(1);
			if (sAction == null) {
				return "Anleitung mit Essen hilfe";
			}

			switch (sAction) {
			case "add":
				return addEssen(t);
			case "del":
				return delEssen(t);
			case "list":
				return getAllEssen();
			case "count":
				return count();
			case "get":
				return getEssenRnd();
			default:
				return
			//@formatter:off
					"Essen Add: Essen hinzufügen\n" +
					"Essen Del: id löschen\n" + 
					"Essen List: alles auflisten\n" +
					"Essen Get: Zufallsessen\n" +
					"Essen Count: Anzahl der Einträge\n";
			//@formatter:on
			}
		} catch (SQLException e) {
			LOGGER.error("menueEssen");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public String getEssenRnd() throws SQLException {
		String sQuery = "select * from essen ORDER BY RANDOM() LIMIT 1;";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			// Es gibt nur einen Eintrag
			return (rs.getString("name"));
		}
		return null;
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String addEssen(Telegram t) throws SQLException {
		String sUpdate = ("INSERT OR REPLACE INTO essen (name, editby, date) VALUES (?,?,?')");

		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setString(1, t.getMessageFullWithoutSecondWord());
		statement.setString(2, t.getFrom());
		statement.setInt(3, t.getDate());
		statement.executeUpdate();

		return "gespeichert";
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private String count() throws SQLException {
		String sQuery = ("SELECT COUNT(*) FROM essen;");
		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			return rs.getString("COUNT(*)") + " Einträge in der DB";
		}

		return "FAIL";
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String delEssen(Telegram t) throws SQLException {
		String sUpdate = "DELETE FROM essen WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, t.getMessageIntPart(2));
		statement.executeUpdate();
		return "DEL";
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String getAllEssen() throws SQLException {
		StringBuilder sb = new StringBuilder();
		String sQuery = "select * from essen;";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			sb.append(rs.getString("id") + "\t");
			sb.append(rs.getString("name") + "\n");
		}
		return sb.toString();
	}

}
