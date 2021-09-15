package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.datatypes.telegram.TodoEntry;
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
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram TodoList");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS todolist (id INTEGER UNIQUE, data TEXT, remind TEXT, active TEXT, editby TEXT, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueTodoList(Telegram t) {
		String sAction = t.getMessageStringPartLow(1);
		if (sAction == null) {
			// @formatter:off
			return "text (add)" + 
					"\n" + 
					"done ID" + "\n" + "undone ID" + "\n" + "delete ID" + "\n" + "get ID" + "\n"
					+ "list" + "\n" + "listall" + "\n" + "listjson" + "\n" + "count" + "\n" + "countall";
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
		case "lj":
		case "listjson":
			return getAllAsJson().toString();
		case "la":
		case "listall":
			return getAllAsList();
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
			String sQuery = "select * from todolist WHERE active NOT LIKE 'DONE';";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();

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

	public String getAllAsList() {
		StringBuilder sb = new StringBuilder();

		try {
			for (TodoEntry tItem : getAll()) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(wipf.jsonToStringAsList(tItem.toJsonRelevantOnly()));
				sb.append("\n");
			}
		} catch (SQLException e) {
			return "fail to get all";
		}

		return sb.toString();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List<TodoEntry> getAll() throws SQLException {
		List<TodoEntry> liTodoE = new ArrayList<>();

		String sQuery = "select * from todolist;";
		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();

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
		return liTodoE;
	}

	/**
	 * @param nId
	 * @return
	 */
	public TodoEntry getById(Integer nId) {
		if (nId == null) {
			return new TodoEntry();
		}

		TodoEntry tItem = new TodoEntry();

		try {
			String sQuery = "select * from todolist WHERE id IS ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			statement.setInt(1, nId);
			ResultSet rs = statement.executeQuery();

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
			String sUpdate = "INSERT OR REPLACE INTO todolist (id, data, editby, date, remind, active) VALUES (?,?,?,?,?,?)";

			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, tE.getId());
			statement.setString(2, tE.getData());
			statement.setString(3, tE.getEditBy());
			statement.setInt(4, tE.getDate());
			statement.setString(5, tE.getRemind());
			statement.setString(6, tE.getActive());
			statement.executeUpdate();

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
			String sUpdate = "DELETE FROM todolist WHERE id LIKE ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, nId);
			statement.executeUpdate();

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
		String sQuery = "SELECT COUNT(*) FROM todolist;";
		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			return rs.getInt("COUNT(*)");
		}
		return -1;
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
			String sQuery = "SELECT COUNT(*) FROM todolist WHERE editby = ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			statement.setInt(1, nTeleID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getString("COUNT(*)") + " Einträge in der DB";
			}
		} catch (Exception e) {
			LOGGER.warn("count todolist " + e);
		}
		return null;
	}

	/**
	 * @param t
	 * @return
	 */
	private String delByID(Integer nID) {
		try {
			String sUpdate = "DELETE FROM todolist WHERE id = ?";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, nID);
			statement.executeUpdate();
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
			String sUpdate = "UPDATE todolist SET active = ? WHERE id = ?";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, sMark);
			statement.setInt(2, nID);
			statement.executeUpdate();
			return "OK";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

}
