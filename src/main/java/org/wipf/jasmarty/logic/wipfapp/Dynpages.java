package org.wipf.jasmarty.logic.wipfapp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.wipfapp.Dynpage;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
public class Dynpages {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	/**
	 * 
	 */
	public void initDB() {
		try {
			String sUpdate = "CREATE TABLE IF NOT EXISTS dynpage (id INTEGER NOT NULL UNIQUE, html TEXT, script TEXT, style TEXT, rechte TEXT, live INTEGER, PRIMARY KEY(id AUTOINCREMENT));";
			sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(Dynpage o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO dynpage (id, html, script, style, rechte, live) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setString(2, o.getHtml());
			statement.setString(3, o.getScript());
			statement.setString(4, o.getStyle());
			statement.setString(5, o.getRechte());
			statement.setBoolean(6, o.getLive());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT OR REPLACE INTO dynpage (id, html, script, style, rechte, live) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, o.getHtml());
			statement.setString(2, o.getScript());
			statement.setString(3, o.getStyle());
			statement.setString(4, o.getRechte());
			statement.setBoolean(5, o.getLive());
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
			save(new Dynpage().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List<Dynpage> getAll() throws SQLException {
		List<Dynpage> o = new LinkedList<>();

		String sQuery = "SELECT * FROM dynpage;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			Dynpage d = new Dynpage();
			d.setId(rs.getInt("id"));
			d.setHtml(rs.getString("html"));
			d.setScript(rs.getString("script"));
			d.setStyle(rs.getString("style"));
			d.setRechte(rs.getString("rechte"));
			d.setLive(rs.getBoolean("live"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @param nDateId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<Dynpage> l = getAll();
		JSONArray ja = new JSONArray();
		for (Dynpage d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @return
	 * @throws SQLExceptiongetAll
	 */
	public Dynpage getById(Integer nId) throws SQLException {
		String sQuery = "SELECT * FROM dynpage WHERE id = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			// Es kann nur einen Eintrag geben
			Dynpage d = new Dynpage();
			d.setId(rs.getInt("id"));
			d.setHtml(rs.getString("html"));
			d.setScript(rs.getString("script"));
			d.setStyle(rs.getString("style"));
			d.setRechte(rs.getString("rechte"));
			d.setLive(rs.getBoolean("live"));
			return d;
		}
		return new Dynpage();
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM dynpage WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

}
