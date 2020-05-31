package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.LcdPage;
import org.wipf.jasmarty.logic.base.MsqlLite;
import org.wipf.jasmarty.logic.base.QMain;

/**
 * @author wipf
 * 
 */
@ApplicationScoped
public class PageVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("PageVerwaltung");
	private Integer nSelectedSite = 1;

	@Inject
	PageConverter pageConverter;

	/**
	 * @throws SQLException
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS pages (id INTEGER UNIQUE, name TEXT, page TEXT, options TEXT);");
		} catch (Exception e) {
			LOGGER.error("init DB");
		}
	}

	/**
	 * 
	 */
	public void writeStartPage() {
		LcdPage p = getPageFromDb(1);
		if (!p.getName().equals("Startseite")) {
			// Wenn es keine Startseite gibt -> schreiben
			LOGGER.info("Keine Startseite gefunden -> Schreibe neu");
			p = new LcdPage();
			p.setId(1);
			p.setName("Startseite");
			p.setOptions("1012");
			p.setLine(0, "jaSmarty");
			p.setLine(1, "");
			p.setLine(2, "von Wipf");
			p.setLine(3, "V$ver()");
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
		p.setLine(3, "V" + QMain.VERSION);
		writePage(p);
	}

	/**
	 * @param jnRoot
	 */
	public void pageToDb(String jnRoot) {
		try {
			pageToDb(new LcdPage().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void pageToDb(LcdPage page) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO pages (id, name, page, options) VALUES ('" + page.getId() + "','"
					+ page.getName() + "','" + page.getPageAsDBString() + "','" + page.getOptions() + "')");
		} catch (SQLException e) {
			LOGGER.warn("pageToDB " + e);
		}
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void delPageFromDb(Integer nId) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM pages WHERE id = " + nId);
		} catch (SQLException e) {
			LOGGER.warn("delPageFromDB " + e);
		}
	}

	/**
	 * @param nId
	 * @return
	 */
	public LcdPage getPageFromDb(int nId) {
		try {
			LcdPage page = new LcdPage();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pages WHERE id = '" + nId + "';");
			page.setId(rs.getInt("id"));
			page.setName(rs.getString("name"));
			page.setStringToPage(rs.getString("page"));
			page.setOptions(rs.getString("options"));
			return page;
		} catch (Exception e) {
			// LOGGER.warn("Page not found: " + nId);
			return new LcdPage();
		}
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
	 */
	public JSONArray getAllPages() {
		JSONArray ja = new JSONArray();
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM pages");
			while (rs.next()) {
				JSONObject entry = new JSONObject();
				entry.put("id", rs.getInt("id"));
				entry.put("name", rs.getString("name"));
				ja.put(entry);
			}
		} catch (Exception e) {
			LOGGER.warn("getAllPages" + e);
		}
		return ja;
	}

}
