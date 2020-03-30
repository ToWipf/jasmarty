package org.wipf.wipfapp.logic.telegram.apps;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.wipfapp.datatypes.Telegram;
import org.wipf.wipfapp.logic.base.MsqlLite;
import org.wipf.wipfapp.logic.telegram.system.MTelegram;

/**
 * @author wipf
 *
 */
public class MTodoList {

	// TODO: edit entry

	private static final Logger LOGGER = Logger.getLogger("MTodoList");

	/**
	 * 
	 */
	public static void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS todolist (id integer primary key autoincrement, data TEXT, remind TEXT, active TEXT, editby TEXT, date INTEGER);");
		} catch (Exception e) {
			LOGGER.warn("initDB todolist " + e);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public static String menueTodoList(Telegram t) {
		// Admin Befehle
		if (MTelegram.isAdminUser(t)) {
			String sAction = t.getMessageStringPart(1);
			if (sAction == null) {
				// @formatter:off
				return 
					"text (add)" + "\n" + 
					"done ID" + "\n" + 
					"undone ID" + "\n" +
					"delete ID" + "\n" +
					"list" +  "\n" +
					"listall" + "\n" +
					"listfull" + "\n" +
					"count" + "\n" +
					"countall";
				// @formatter:on
			}

			switch (sAction) {
			// case "a":
			// case "add":
			// return add(t);
			case "d":
			case "done":
				return markDone(t.getMessageIntPart(2));
			case "u":
			case "undone":
				return markUnDone(t.getMessageIntPart(2));
			case "del":
			case "delete":
				return delByID(t.getMessageIntPart(2));
			case "l":
			case "list":
				return getAllUnDoneByUser(t.getFromIdOnly());
			case "la":
			case "listall":
				return getAllByUser(t.getFromIdOnly());
			case "ev":
			case "everything":
				return getAll();
			case "lf":
			case "listfull":
				return getAllFull();
			case "c":
			case "count":
				return count(t.getFromIdOnly());
			case "ca":
			case "countall":
				return countAll();
			default:
				return addByTelegram(t);
			}
		}
		return "Du kannst keine Todo-Liste anlegen";
	}

	/**
	 * @return
	 */
	public static String getAllFull() {
		try {
			StringBuilder slog = new StringBuilder();
			int n = 0;
			Statement stmt = MsqlLite.getDB();

			ResultSet rs = stmt.executeQuery("SELECT * FROM todolist");

			while (rs.next()) {
				n++;
				// Timestamp zu datum
				Date date = new Date(rs.getLong("date") * 1000);
				StringBuilder sb = new StringBuilder();

				sb.append(n + ":\n");
				sb.append("id:  \t" + rs.getString("id") + "\n");
				sb.append("data: \t" + rs.getString("data") + "\n");
				sb.append("remind:\t" + rs.getString("remind") + "\n");
				sb.append("active: \t" + rs.getString("active") + "\n");
				sb.append("editby:\t" + rs.getString("editby") + "\n");
				sb.append("date:\t" + date + "\n");
				sb.append("----------------\n\n");
				slog.insert(0, sb);
			}
			rs.close();
			return slog.toString();
		} catch (Exception e) {
			LOGGER.warn("getAllFull todolist" + e);
			return "FAIL";
		}

	}

	/**
	 * @return
	 */
	public static String getAll() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from todolist;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("data") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all todolist" + e);
		}
		return "Fehler";
	}

	/**
	 * @param nTeleID
	 * @return
	 */
	public static String getAllByUser(Integer nTeleID) {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from todolist WHERE editby = " + nTeleID + ";");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("data") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all todolist" + e);
		}
		return "Fehler";
	}

	/**
	 * @param nTeleID
	 * @return
	 */
	public static String getAllUnDoneByUser(Integer nTeleID) {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("select * from todolist WHERE editby = " + nTeleID + " AND active NOT LIKE 'DONE';");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("data") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all todolist" + e);
		}
		return "Fehler";
	}

	/**
	 * @return
	 */
	public static String getAllAsJson() {
		try {
			JSONArray json = new JSONArray();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from todolist;");
			while (rs.next()) {
				JSONObject entry = new JSONObject();
				entry.put("id", rs.getString("id"));
				entry.put("data", rs.getString("data"));
				entry.put("editby", rs.getString("editby"));
				entry.put("active", rs.getString("active"));
				entry.put("remind", rs.getString("remind"));
				entry.put("date", rs.getString("date"));
				json.put(entry);
			}
			rs.close();
			return json.toString();

		} catch (Exception e) {
			LOGGER.warn("get all json todolist" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 */
	private static String addByTelegram(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			//@formatter:off
			stmt.execute("INSERT OR REPLACE INTO todolist (data, editby, date, active) VALUES " +
					"('" + t.getMessageStringFirst() +
					"','" + t.getFromIdOnly() +
					"','"+ t.getDate() +
					"','"+ "NEW" +
					"')");
			//@formatter:on
			return "gespeichert";
		} catch (Exception e) {
			LOGGER.warn("add todo " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 */
	private static String countAll() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM todolist;");
			return rs.getString("COUNT(*)") + " Einträge in der DB";
		} catch (Exception e) {
			LOGGER.warn("count todolist " + e);
			return null;
		}
	}

	private static String count(Integer nTeleID) {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM todolist WHERE editby = " + nTeleID + ";");
			return rs.getString("COUNT(*)") + " Einträge in der DB";
		} catch (Exception e) {
			LOGGER.warn("count todolist " + e);
			return null;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private static String delByID(Integer nID) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM todolist WHERE id = " + nID);
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private static String markDone(Integer nID) {
		return mark("DONE", nID);
	}

	/**
	 * @param t
	 * @return
	 */
	private static String markUnDone(Integer nID) {
		return mark("UNDONE", nID);
	}

	/**
	 * @param sMark
	 * @param nID
	 * @return
	 */
	private static String mark(String sMark, Integer nID) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("UPDATE todolist SET active ='" + sMark + "' WHERE id = " + nID);
			return "OK";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

}
