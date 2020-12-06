package org.wipf.jasmarty.logic.jasmarty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.LcdPage;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 * 
 */
@ApplicationScoped
public class PageVerwaltung {

	@Inject
	PageConverter pageConverter;
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("PageVerwaltung");
	private Integer nSelectedSite = 1;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS pages (id INTEGER UNIQUE, name TEXT, page TEXT, options TEXT);";
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void writeStartPage() throws SQLException {
		LcdPage p = getPageFromDb(1);
		if (!p.getName().equals("Startseite " + MainHome.VERSION)) {
			// Wenn es keine Startseite gibt -> schreiben
			LOGGER.info("Keine Startseite für die Version " + MainHome.VERSION + " gefunden -> Schreibe neu");
			p = new LcdPage();
			p.setId(1);
			p.setName("Startseite " + MainHome.VERSION);
			p.setOptions("1120");
			p.setLine(0, "$time(HH:mm:ss)");
			p.setLine(1, "$time(dd MMMM yyyy)");
			p.setLine(2, "$time(EEEEE)$pos(17)$char(4)$char(5)$char(6)");
			p.setLine(3, "V$ver()$pos(18)$char(7)$char(8)$char(9)");
			pageToDb(p);
		}
		LOGGER.info("Startseite laden");
		writePage(p);
	}

	/**
	 * 
	 */
	public void writeExitPage() {
		// Seite nicht aus der db laden -> Fester inhalt
		LcdPage p = new LcdPage();
		p.setId(0);
		p.setOptions("1012");
		p.setLine(0, "Beendet");
		p.setLine(1, "");
		p.setLine(2, "-wipf-");
		p.setLine(3, "V" + MainHome.VERSION);
		writePage(p);
	}

	/**
	 * @param jnRoot
	 * @throws SQLException
	 */
	public void pageToDb(String jnRoot) throws SQLException {
		pageToDb(new LcdPage().setByJson(jnRoot));
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void pageToDb(LcdPage page) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO pages (id, name, page, options) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, page.getId());
		statement.setString(2, page.getName());
		statement.setString(3, page.getPageAsDBString());
		statement.setString(4, page.getOptions());
		statement.executeUpdate();
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void delPageFromDb(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM pages WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nId
	 * @return
	 */
	public LcdPage getPageFromDb(int nId) {
		LcdPage page = new LcdPage();
		try {

			String sQuery = "SELECT * FROM pages WHERE id = ?;";
			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sQuery);
			statement.setInt(1, nId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// Es gibt nur einen oder keinen Eintrag
				page.setId(rs.getInt("id"));
				page.setName(rs.getString("name"));
				page.setStringToPage(rs.getString("page"));
				page.setOptions(rs.getString("options"));
				return page;
			}
		} catch (Exception e) {
			// LOGGER.warn("Page not found: " + nId);
			// TODO
		}
		return page;
	}

	/**
	 * @param page
	 */
	public void writePage(LcdPage page) {
		try {
			pageConverter.selectToNewPage(page);
		} catch (Exception e) {
			LOGGER.warn("Seiten init fehlgeschlagen. Seiten id: '" + page.getId() + "' name: '" + page.getName()
					+ "' fehler: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * @param nId
	 */
	public void selectPage(int nId) {
		nSelectedSite = nId;
		writePage(getPageFromDb(nId));
	}

	/**
	 * @param sId
	 */
	public void selectPage(String sId) {
		selectPage(Integer.valueOf(sId));
	}

	/**
	 * 
	 */
	public void nextPage() {
		selectPage(nSelectedSite + 1);
		// TODO limit

	}

	/**
	 * 
	 */
	public void lastPage() {
		selectPage(nSelectedSite - 1);
		// TODO limit
	}

	/**
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public JSONArray getAllPages() throws JSONException, SQLException {
		JSONArray ja = new JSONArray();

		String sQuery = "SELECT id, name FROM pages";
		ResultSet rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			JSONObject entry = new JSONObject();
			entry.put("id", rs.getInt("id"));
			entry.put("name", rs.getString("name"));
			ja.put(entry);
		}
		return ja;
	}

}
