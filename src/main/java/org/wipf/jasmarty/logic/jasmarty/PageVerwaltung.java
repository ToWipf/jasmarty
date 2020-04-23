package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.LcdPage;
import org.wipf.jasmarty.logic.base.App;
import org.wipf.jasmarty.logic.base.MsqlLite;

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
		LcdPage p = new LcdPage();
		p.setId(1);
		p.setName("Startseite");
		p.setOptions("1012");
		p.setLine(0, "jaSmarty");
		p.setLine(1, "");
		p.setLine(2, "by Wipf");
		p.setLine(3, "V" + App.VERSION);
		writePage(p);

		// TODO save this page to db ever?

		pageToDB(p);
	}

	/**
	 * 
	 */
	public void writeExitPage() {
		LcdPage p = new LcdPage();
		p.setId(0);
		p.setOptions("1001");
		p.setLine(0, "Beendet");
		p.setLine(1, "");
		p.setLine(2, "");
		p.setLine(3, "-wipf-");
		writePage(p);
		// TODO load this page from db

	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void pageToDB(LcdPage page) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO pages (id, name, page, options) VALUES ('" + page.getId() + "','"
					+ page.getName() + "','" + page.getPageAsDBString() + "','" + page.getOptions() + "')");
		} catch (SQLException e) {
			LOGGER.warn("pageToDB " + e);
		}
	}

	/**
	 * TODO löschen
	 * 
	 * @param sName
	 * @param sPage
	 * @throws SQLException
	 */
	public void newPageToDB(String sName, String sPage, String sOptions) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO pages (name, page, options) VALUES ('" + sName + "','" + sPage + "','"
					+ sOptions + "')");
		} catch (SQLException e) {
			LOGGER.warn("newPageToDB " + e);
		}
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void delPageFromDB(Integer nId) {
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
	public LcdPage getPageFromDB(int nId) {
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
		pageConverter.selectToNewPage(page);
	}

	/**
	 * @param nId
	 */
	public void selectPage(int nId) {
		nSelectedSite = nId;
		writePage(getPageFromDB(nId));
	}

	/**
	 * @param sId
	 */
	public void selectPage(String sId) {
		selectPage(Integer.valueOf(sId));
	}

	/**
	 * @param jnRoot
	 */
	public void newPageToDB(String jnRoot) {
		try {
			pageToDB(new LcdPage().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}

	/**
	 * 
	 */
	public void nextPage() {
		selectPage(nSelectedSite + 1);

	}

	/**
	 * 
	 */
	public void lastPage() {
		selectPage(nSelectedSite - 1);

	}

}
