package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.datatypes.TodoEntry;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppTodoList {

	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram TodoList");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS todolist (id INTEGER UNIQUE, data TEXT, remind TEXT, active TEXT, editby TEXT, date INTEGER);");
		stmt.close();
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueTodoList(Telegram t) {
		String sAction = t.getMessageStringPartLow(1);
		if (sAction == null) {
			// @formatter:off
			return "text (add)" + "\n" + "done ID" + "\n" + "undone ID" + "\n" + "delete ID" + "\n" + "get ID" + "\n"
					+ "list" + "\n" + "listall" + "\n" + "listfull" + "\n" + "count" + "\n" + "countall";
			// @formatter:on
		}

		switch (sAction) {
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
			return getAllUnDone().toString();
		case "la":
		case "listall":
		case "ev":
		case "everything":
		case "lf":
		case "listfull":
			return getAllAsJson().toString();
		case "c":
		case "count":
			return count(t.getFromIdOnly());
		case "ca":
		case "countall":
			return countAll();
		case "get":
			return wipf.jsonToStringAsList(getById(t.getMessageIntPart(2)).toJson());
		default:
			return saveItem(t).toString();
		}

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllUnDone() {
		List<TodoEntry> liTodoE = new ArrayList<>();

		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from todolist WHERE active NOT LIKE 'DONE';");

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
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson A: " + e);
		}

		JSONArray ja = new JSONArray();
		try {
			for (TodoEntry tItem : liTodoE) {
				ja.put(tItem.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson B: " + e);
		}
		return ja;
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
			LOGGER.warn("getAllAsJson " + e);
			e.printStackTrace();
		}
		return ja;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List<TodoEntry> getAll() throws SQLException {
		List<TodoEntry> liTodoE = new ArrayList<>();

		Statement stmt = SqlLite.getDB();
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
		stmt.close();
		return liTodoE;
	}

	/**
	 * @param nId
	 * @return
	 */
	public TodoEntry getById(int nId) {
		Statement stmt = SqlLite.getDB();
		TodoEntry tItem = new TodoEntry();

		// TODO init?
		tItem.setActive("");
		tItem.setAntwort("");
		tItem.setChatID(-1);
		tItem.setData("");
		tItem.setEditBy("");
		tItem.setEditBy("");
		tItem.setId(-1);
		tItem.setMessage("");
		tItem.setMid(-1);
		tItem.setOptions("");
		tItem.setRemind("");
		tItem.setType("");

		try {
			ResultSet rs = stmt.executeQuery("select * from todolist WHERE id IS '" + nId + "';");

			while (rs.next()) {
				TodoEntry entry = new TodoEntry();
				entry.setId(rs.getInt("id"));
				entry.setData(rs.getString("data"));
				entry.setEditBy(rs.getString("editby"));
				entry.setActive(rs.getString("active"));
				entry.setRemind(rs.getString("remind"));
				entry.setDate(rs.getInt("date"));
				tItem = entry;
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.warn("ById " + e);
		}
		return tItem;
	}

	/**
	 * @param tE
	 * @return id
	 */
	public Integer saveItem(TodoEntry tE) {
		try {
			Statement stmt = SqlLite.getDB();
			// @formatter:off
			stmt.execute("INSERT OR REPLACE INTO todolist (id, data, editby, date, remind, active) VALUES " + "('"
					+ tE.getId() + "','" + tE.getData() + "','" + tE.getEditBy() + "','" + tE.getDate() + "','"
					+ tE.getRemind() + "','" + tE.getActive() + "')");
			// @formatter:on
			stmt.close();
			return tE.getId();
		} catch (Exception e) {
			LOGGER.warn("save " + e);
			return null;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private Integer saveItem(Telegram t) {
		int nId = genNextId();
		TodoEntry te = new TodoEntry();
		te.setData(t.getMessageFullWithoutFirstWord());
		te.setEditBy(t.getFromIdOnly().toString());
		te.setDate(t.getDate());
		te.setRemind("");
		te.setActive("NEW");
		te.setId(nId);
		saveItem(te);

		return nId;
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public Integer saveItem(String jnRoot) {
		return saveItem(new TodoEntry().setByJson(jnRoot));
	}

	/**
	 * @param nId
	 */
	public void deleteItem(Integer nId) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM todolist WHERE id LIKE '" + nId + "';");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("del todo " + e);
		}
	}

	/**
	 * @return
	 */
	private int genNextId() {
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
	 * @return
	 * @throws SQLException
	 */
	private Integer countItems() throws SQLException {
		Statement stmt = SqlLite.getDB();
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
			Statement stmt = SqlLite.getDB();
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
			Statement stmt = SqlLite.getDB();
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("UPDATE todolist SET active ='" + sMark + "' WHERE id = " + nID);
			return "OK";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

}
