package org.wipf.jasmarty.logic.telegram.extensions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.datatypes.TodoEntry;
import org.wipf.jasmarty.logic.base.MsqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppTodoList {

	// TODO: edit entry

	private static final Logger LOGGER = Logger.getLogger("MTodoList");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			// stmt.executeUpdate("DROP TABLE todolist;");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS todolist (id INTEGER UNIQUE, data TEXT, remind TEXT, active TEXT, editby TEXT, date INTEGER);");
		} catch (Exception e) {
			LOGGER.warn("initDB todolist " + e);
		}
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public boolean setTodo(String jnRoot) {
		return saveEntry(new TodoEntry().setByJson(jnRoot));
	}

	/**
	 * @param t
	 * @return
	 */
	public String menueTodoList(Telegram t) {
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
		case "ev":
		case "everything":
			return getAllAsJson().toString();
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

	/**
	 * @return
	 */
	public String getAllFull() {
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
	 * @param nTeleID
	 * @return
	 */
	public String getAllUnDoneByUser(Integer nTeleID) {
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
	public JSONArray getAllAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (TodoEntry tItem : getAll()) {
				ja.put(tItem.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson" + e);
		}
		return ja;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List<TodoEntry> getAll() throws SQLException {
		List<TodoEntry> liTodoE = new ArrayList<>();

		Statement stmt = MsqlLite.getDB();
		ResultSet rs = stmt.executeQuery("select * from todolist;");

		while (rs.next()) {
			TodoEntry entry = new TodoEntry();
			entry.setId(rs.getInt("id"));
			entry.setData(rs.getString("data"));
			entry.setEditBy(rs.getString("editby"));
			entry.setActive(rs.getString("active"));
			entry.setRemind(rs.getString("remind"));
			entry.setDate(rs.getInt("date"));
			liTodoE.add(entry);
		}
		rs.close();

		return liTodoE;
	}

	/**
	 * @param tE
	 * @return
	 */
	public boolean saveEntry(TodoEntry tE) {
		try {
			Statement stmt = MsqlLite.getDB();
			//@formatter:off
			stmt.execute("INSERT OR REPLACE INTO todolist (id, data, editby, date, remind, active) VALUES " +
					"('"  + tE.getId() +
					"','" + tE.getData() +
					"','" + tE.getEditBy() +
					"','" + tE.getDate() +
					"','" + tE.getRemind() +
					"','" + tE.getActive() +
					"')");
			//@formatter:on
			return true;
		} catch (Exception e) {
			LOGGER.warn("save todo " + e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param nId
	 */
	public void delete(Integer nId) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM todolist WHERE id LIKE '" + nId + "';");
		} catch (Exception e) {
			LOGGER.warn("del todo " + e);
		}
	}

	/**
	 * @return
	 */
	private int getNextId() {
		int nNextId = 0;
		try {
			for (TodoEntry te : this.getAll()) {
				if (te.getId() > nNextId) {
					nNextId = te.getId();
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getNextId " + e);
		}
		return nNextId + 1;
	}

	/**
	 * @param t
	 * @return
	 */
	private String addByTelegram(Telegram t) {
		int nId = getNextId();
		TodoEntry te = new TodoEntry();
		te.setData(t.getMessageStringFirst());
		te.setEditBy(t.getFromIdOnly().toString());
		te.setDate(t.getDate());
		te.setRemind("");
		te.setActive("NEW");
		te.setId(nId);
		saveEntry(te);

		return "gespeichert Id: " + nId;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private Integer countItems() throws SQLException {
		Statement stmt = MsqlLite.getDB();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM todolist;");
		return rs.getInt("COUNT(*)");
	}

	/**
	 * @return
	 */
	private String countAll() {
		try {
			return countItems() + " Einträge in der DB";
		} catch (Exception e) {
			LOGGER.warn("count todolist " + e);
			return null;
		}
	}

	/**
	 * @param nTeleID
	 * @return
	 */
	private String count(Integer nTeleID) {
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
	private String delByID(Integer nID) {
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
	private String markDone(Integer nID) {
		return mark("DONE", nID);
	}

	/**
	 * @param t
	 * @return
	 */
	private String markUnDone(Integer nID) {
		return mark("UNDONE", nID);
	}

	/**
	 * @param sMark
	 * @param nID
	 * @return
	 */
	private String mark(String sMark, Integer nID) {
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
