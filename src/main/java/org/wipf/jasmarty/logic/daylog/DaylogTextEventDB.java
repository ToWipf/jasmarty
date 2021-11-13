package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.daylog.DaylogTextEvent;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogTextEventDB {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS daylogTextEvent (id INTEGER NOT NULL UNIQUE, dateid INTEGER, typ TEXT, text TEXT, PRIMARY KEY(id AUTOINCREMENT));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(DaylogTextEvent o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO daylogTextEvent (id, dateid, typ, text) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setInt(2, o.getDateId());
			statement.setString(3, o.getTyp());
			statement.setString(4, o.getText());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT OR REPLACE INTO daylogTextEvent (dateid, typ, text) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getDateId());
			statement.setString(2, o.getTyp());
			statement.setString(3, o.getText());
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
			save(new DaylogTextEvent().setByJson(jnRoot));
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
	public List<DaylogTextEvent> get(Integer nDateId) throws SQLException {
		List<DaylogTextEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogTextEvent WHERE dateid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nDateId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogTextEvent d = new DaylogTextEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setText(rs.getString("text"));
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
		List<DaylogTextEvent> l = get(nDateId);
		JSONArray ja = new JSONArray();
		for (DaylogTextEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM daylogTextEvent WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogTextEvent> getAll() throws SQLException {
		List<DaylogTextEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogTextEvent";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogTextEvent d = new DaylogTextEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setText(rs.getString("text"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<DaylogTextEvent> l = getAll();
		JSONArray ja = new JSONArray();
		for (DaylogTextEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
