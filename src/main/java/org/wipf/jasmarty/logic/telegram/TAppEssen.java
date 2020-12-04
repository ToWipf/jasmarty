package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Telegram;
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
		sqlLite.getNewDb().prepareStatement(sUpdate).executeUpdate();
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

		ResultSet rs = sqlLite.getNewDb().prepareStatement(sQuery).executeQuery();
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
		Statement stmt = SqlLite.getDB();
		//@formatter:off
			stmt.execute("INSERT OR REPLACE INTO essen (name, editby, date) VALUES " +
					"('" + t.getMessageFullWithoutSecondWord() +
					"','" + t.getFrom() +
					"','"+ t.getDate() +
					"')");
			//@formatter:on
		stmt.close();
		return "gespeichert";
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private String count() throws SQLException {
		Statement stmt = SqlLite.getDB();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM essen;");
		String s = rs.getString("COUNT(*)") + " Einträge in der DB";
		stmt.close();
		return s;
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String delEssen(Telegram t) throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.execute("DELETE FROM essen WHERE id = " + t.getMessageIntPart(2));
		stmt.close();
		return "DEL";
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String getAllEssen() throws SQLException {
		StringBuilder sb = new StringBuilder();

		Statement stmt = SqlLite.getDB();
		ResultSet rs = stmt.executeQuery("select * from essen;");
		while (rs.next()) {
			sb.append(rs.getString("id") + "\t");
			sb.append(rs.getString("name") + "\n");
		}
		stmt.close();
		return sb.toString();
	}

}
