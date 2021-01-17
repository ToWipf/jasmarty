package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageDescription;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * Seiten Speichern und laden
 * 
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864PageVerwaltung {

	@Inject
	SqlLite sqlLite;
	@Inject
	Lcd12864PageConverter converter;

	/**
	 * Jede Page hat einen Festen Pixelspeicher und eine Beschreibung (Bauplan) des
	 * Inhaltes
	 * 
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS lcd_pages12864 (id INTEGER UNIQUE, name TEXT, static TEXT, dynamic TEXT);";
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void save(Lcd12864PageDescription page) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO lcd_pages12864 (id, name, static, dynamic) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, page.getId());
		statement.setString(2, page.getName());
		statement.setString(3, page.getStatic().toString());
		statement.setString(4, page.getDynamic().toString());
		statement.executeUpdate();
	}

	/**
	 * @param jnRoot
	 * @throws SQLException
	 */
	public void save(String jnRoot) throws SQLException {
		save(new Lcd12864PageDescription().setByJson(jnRoot));
	}

	/**
	 * @return
	 * 
	 */
	public Lcd12864PageDescription load(int nId) {
		Lcd12864PageDescription pd = new Lcd12864PageDescription();
		try {

			String sQuery = "SELECT * FROM lcd_pages12864 WHERE id = ?;";
			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sQuery);
			statement.setInt(1, nId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// Es gibt nur einen oder keinen Eintrag
				pd.setId(rs.getInt("id"));
				pd.setName(rs.getString("name"));
				pd.setStatic(rs.getString("static"));
				pd.setDynamic(rs.getString("dynamic"));
				// pd.setStatic(rs.getArray("static").toString()); TODO
				return pd;
			}
		} catch (Exception e) {
			// LOGGER.warn("Page not found: " + nId);
			// TODO
		}
		return pd;
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM lcd_pages12864 WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nId
	 */
	public void select(int nId) {
		converter.setPageDescription(load(nId));
	}

}
