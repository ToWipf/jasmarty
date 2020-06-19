package org.wipf.jasmarty.logic.telegram.messageEdit;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.TeleMsg;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMotd {

	private static final Logger LOGGER = Logger.getLogger("Telegram Motd");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemotd (id integer primary key autoincrement, text TEXT, editby TEXT, date INTEGER);");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public String delMotd(TeleMsg t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM telemotd WHERE id = " + t.getMessageIntPart(1));
			stmt.close();
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 */
	public String addMotd(TeleMsg t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemotd (text, editby, date) VALUES " + "('"
					+ t.getMessageFullWithoutFirstWord() + "','" + t.getFrom() + "','" + t.getDate() + "')");
			stmt.close();
			return "IN";
		} catch (Exception e) {
			LOGGER.warn("add telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 * 
	 *         TODO del this
	 */
	public String countMotd() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemotd;");
			String s = rs.getString("COUNT(*)") + " Motds in der DB";
			stmt.close();
			return s;
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
			String s = null;

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd ORDER BY RANDOM() LIMIT 1");
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				s = (rs.getString("text"));
			}
			stmt.close();
			return s;

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

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("text") + "\n");
			}
			stmt.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

}
