package org.wipf.jasmarty.logic.liste;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.liste.ListeType;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author Wipf
 *
 */
public class ListeTypeDB {

	@Inject
	Wipf wipf;
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Neue TodoListe");
	private static String TABLENAME = "liste";

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS " + TABLENAME
				+ " (id INTEGER primary key autoincrement UNIQUE, data TEXT);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public void save(String jnRoot) throws SQLException {
		save(new ListeType().setByJson(jnRoot));
	}

	/**
	 * @param o
	 * @return
	 * @throws SQLException
	 */
	public void save(ListeType o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO " + TABLENAME + " (id, typename) VALUES (?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setString(2, o.getTypename());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT INTO " + TABLENAME + " (typename) VALUES (?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, o.getTypename());
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
	public List<ListeType> getAll() throws SQLException {
		List<ListeType> lr = new LinkedList<>();
		String sQuery = "select * from " + TABLENAME + ";";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			ListeType o = new ListeType();
			o.setId(rs.getInt("id"));
			o.setTypename(rs.getString("typename"));
			lr.add(o);
		}
		return lr;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<ListeType> l = getAll();
		JSONArray ja = new JSONArray();
		for (ListeType d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
