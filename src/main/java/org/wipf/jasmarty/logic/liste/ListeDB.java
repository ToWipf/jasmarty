package org.wipf.jasmarty.logic.liste;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.liste.Liste;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class ListeDB {

	@Inject
	Wipf wipf;
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Neue TodoListe");
	private static String TABLENAME = "listeType";

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS " + TABLENAME
				+ " (id INTEGER primary key autoincrement UNIQUE, typename TEXT, typeid INTEGER, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public void save(String jnRoot) throws SQLException {
		save(new Liste().setByJson(jnRoot));
	}

	/**
	 * @param o
	 * @return
	 * @throws SQLException
	 */
	public void save(Liste o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO " + TABLENAME + " (id, data, typeid, data) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setString(2, o.getData());
			statement.setInt(3, o.getTypeId());
			statement.setInt(4, o.getDate());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT INTO " + TABLENAME + " (data, typeid, data) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, o.getData());
			statement.setInt(2, o.getTypeId());
			statement.setInt(3, o.getDate());
			statement.executeUpdate();
		}
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM " + TABLENAME + " WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public List<Liste> getAll() throws SQLException {
		List<Liste> lr = new LinkedList<>();
		String sQuery = "select * from " + TABLENAME + ";";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			Liste o = new Liste();
			o.setId(rs.getInt("id"));
			o.setData(rs.getString("data"));
			o.setTypeId(rs.getInt("typeid"));
			o.setDate(rs.getInt("date"));
			lr.add(o);
		}
		return lr;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<Liste> l = getAll();
		JSONArray ja = new JSONArray();
		for (Liste d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
