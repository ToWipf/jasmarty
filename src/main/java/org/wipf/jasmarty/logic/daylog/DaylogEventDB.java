package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.daylog.DaylogEvent;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogEventDB {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS DaylogTextEvent (id INTEGER NOT NULL UNIQUE, dateid INTEGER, typ TEXT, text TEXT, PRIMARY KEY(id AUTOINCREMENT));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(DaylogEvent o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO DaylogTextEvent (id, dateid, typ, text) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setInt(2, o.getDateId());
			statement.setString(3, o.getTyp());
			statement.setString(4, o.getText());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT OR REPLACE INTO DaylogTextEvent (dateid, typ, text) VALUES (?,?,?)";
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
			save(new DaylogEvent().setByJson(jnRoot));
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
	public List<DaylogEvent> getByDateId(Integer nDateId) throws SQLException {
		List<DaylogEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM DaylogTextEvent WHERE dateid = ? ORDER BY typ;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nDateId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogEvent d = new DaylogEvent();
			d.setId(rs.getInt("id"));
			d.setDateId(rs.getInt("dateid"));
			d.setTyp(rs.getString("typ"));
			d.setText(rs.getString("text"));
			o.add(d);
		}

		return o;
	}

	/**
	 * @param nTypId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogEvent> getAllByTypId(String sTypIds) throws SQLException {
		List<DaylogEvent> o = new LinkedList<>();
		String sQuery = "SELECT * FROM DaylogTextEvent WHERE typ IN (" + sTypIds + ")";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			DaylogEvent d = new DaylogEvent();
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
		List<DaylogEvent> l = getByDateId(nDateId);
		JSONArray ja = new JSONArray();
		for (DaylogEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM DaylogTextEvent WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogEvent> getAll() throws SQLException {
		List<DaylogEvent> o = new LinkedList<>();

		String sQuery = "SELECT * FROM DaylogTextEvent";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogEvent d = new DaylogEvent();
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
		List<DaylogEvent> l = getAll();
		JSONArray ja = new JSONArray();
		for (DaylogEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sTypIds = "1,2,3,9";
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getStats(String sTypIds) throws SQLException {
		JSONArray ar = new JSONArray();
		String sQuery = "SELECT COUNT(*) anz, * from daylogTextEvent where typ IN (" + sTypIds
				+ ") GROUP by text ORDER by anz DESC";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		// statement.setString(1, sTypIds);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			JSONObject oo = new JSONObject();
			oo.put("anz", rs.getInt("anz"));
			oo.put("first_id", rs.getInt("id"));
			oo.put("first_dateid", rs.getInt("dateid"));
			oo.put("frist_typ", rs.getInt("typ"));
			oo.put("text", rs.getString("text"));
			ar.put(oo);
		}
		return ar;
	}

	/**
	 * @param sSearch
	 * @param sType
	 * @return
	 * @throws SQLException
	 */
	public LinkedHashSet<String> getTextBySearchAndType(String sSearch, String sType) throws SQLException {
		LinkedHashSet<String> o = new LinkedHashSet<>();

		String sQuery = "SELECT DISTINCT text FROM daylogTextEvent WHERE text LIKE ? AND typ = ? ORDER BY length(text) ASC;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setString(1, "%" + sSearch + "%");
		statement.setString(2, sType);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			o.add(rs.getString("text"));
		}

		return o;
	}

}
