package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd2004Page;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864PageVW {
	@Inject
	SqlLite sqlLite;

	/**
	 * Jede Page hat optional einen Festen Pixelspeicher oder eine beschreibung
	 * (Bauplan) des Inhaltes
	 * 
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS lcd_pages12864 (id INTEGER UNIQUE, type TEXT, name TEXT, page_static TEXT, page_dyn TEXT);";
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void save(Lcd2004Page page) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO pages (id, name, page, options) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, page.getId());
		statement.setString(2, page.getName());
		statement.setString(3, page.getPageAsDBString());
		statement.setString(4, page.getOptions());
		statement.executeUpdate();
	}

	/**
	 * 
	 */
	public void load() {

	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM pages WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

}
