package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.daylog.DaylogBoolEvent;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogBoolEventDB {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS daylogBoolEvent (id INTEGER NOT NULL UNIQUE, dateid INTEGER, typ TEXT, bool INTEGER, PRIMARY KEY(id AUTOINCREMENT));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(DaylogBoolEvent o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO daylogBoolEvent (id, dateid, typ, bool) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setInt(2, o.getDateId());
			statement.setString(3, o.getTyp());
			statement.setBoolean(4, o.getBool());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT OR REPLACE INTO daylogBoolEvent (dateid, typ, bool) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getDateId());
			statement.setString(2, o.getTyp());
			statement.setBoolean(3, o.getBool());
			statement.executeUpdate();
		}
	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		try {
			save(new DaylogBoolEvent().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogBoolEvent> get(Integer nDateId) throws SQLException {
		List<DaylogBoolEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogBoolEvent WHERE dateid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nDateId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogBoolEvent d = new DaylogBoolEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setBool(rs.getBoolean("bool"));
			o.add(d);
		}

		return o;
	}

	/**
	 * @param nDateId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAsJson(Integer nDateId) throws SQLException {
		List<DaylogBoolEvent> l = get(nDateId);
		JSONArray ja = new JSONArray();
		for (DaylogBoolEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM daylogBoolEvent WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogBoolEvent> getAll() throws SQLException {
		List<DaylogBoolEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogBoolEvent";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogBoolEvent d = new DaylogBoolEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setBool(rs.getBoolean("bool"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<DaylogBoolEvent> l = getAll();
		JSONArray ja = new JSONArray();
		for (DaylogBoolEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
