package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppEssen {

	private static final Logger LOGGER = Logger.getLogger("Telegram Essen");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS essen (id integer primary key autoincrement, type TEXT, name TEXT, options TEXT, editby TEXT, date INTEGER);");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("initDB " + e);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public String menueEssen(Telegram t) {
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
	}

	/**
	 * @param t
	 * @return
	 */
	private String addEssen(Telegram t) {
		try {
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
		} catch (Exception e) {
			LOGGER.warn("add essen " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 */
	private String count() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM essen;");
			String s = rs.getString("COUNT(*)") + " Einträge in der DB";
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("count essen " + e);
			return null;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String delEssen(Telegram t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM essen WHERE id = " + t.getMessageIntPart(2));
			stmt.close();
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete essen" + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String getAllEssen() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from essen;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("name") + "\n");
			}
			stmt.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all essen" + e);
		}
		return "Fehler";
	}

	/**
	 * @return
	 */
	public String getEssenRnd() {
		try {
			String s = null;

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from essen ORDER BY RANDOM() LIMIT 1");
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				s = (rs.getString("name"));
			}
			stmt.close();
			return s;

		} catch (Exception e) {
			LOGGER.warn("get essen rnd " + e);
			return "Fehler";
		}
	}
}
