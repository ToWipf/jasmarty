package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.daylog.DaylogNumberEvent;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogNumberEventDB {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS daylogNumberEvent (id INTEGER NOT NULL UNIQUE, dateid INTEGER, typ TEXT, number INTEGER, PRIMARY KEY(id AUTOINCREMENT));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(DaylogNumberEvent o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO daylogNumberEvent (id, dateid, typ, number) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setInt(2, o.getDateId());
			statement.setString(3, o.getTyp());
			statement.setInt(4, o.getNumber());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT OR REPLACE INTO daylogNumberEvent (dateid, typ, number) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getDateId());
			statement.setString(2, o.getTyp());
			statement.setInt(3, o.getNumber());
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
			save(new DaylogNumberEvent().setByJson(jnRoot));
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
	public List<DaylogNumberEvent> get(Integer nDateId) throws SQLException {
		List<DaylogNumberEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogNumberEvent WHERE date = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nDateId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogNumberEvent d = new DaylogNumberEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setNumber(rs.getInt("number"));
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
		List<DaylogNumberEvent> l = get(nDateId);
		JSONArray ja = new JSONArray();
		for (DaylogNumberEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM daylogNumberEvent WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

}
