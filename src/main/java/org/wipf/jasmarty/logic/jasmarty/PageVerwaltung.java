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
	private Integer nSelectedSite;

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
		try {
			LcdPage p = new LcdPage();
			p.setId(0);
			p.setOptions("1012");
			p.setLine(0, "jaSmarty");
			p.setLine(1, "");
			p.setLine(2, "by Wipf");
			p.setLine(3, "V: " + App.VERSION);
			writePage(p);
			// TODO save this page to db

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void writeExitPage() {
		try {
			LcdPage p = new LcdPage();
			p.setId(0);
			p.setOptions("1001");
			p.setLine(0, "Beendet");
			p.setLine(1, "");
			p.setLine(2, "");
			p.setLine(3, "-wipf-");
			writePage(p);
			// TODO save this page to db

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void pageToDB(LcdPage page) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO pages (id, name, page, options) VALUES ('" + page.getId() + "','"
				+ page.getName() + "','" + page.getPageAsDBString() + "','" + page.getOptions() + "')");
	}

	/**
	 * TODO löschen
	 * 
	 * @param sName
	 * @param sPage
	 * @throws SQLException
	 */
	public void newPageToDB(String sName, String sPage, String sOptions) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO pages (name, page, options) VALUES ('" + sName + "','" + sPage + "','"
				+ sOptions + "')");
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void delPageFromDB(Integer nId) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("DELETE FROM pages WHERE id = " + nId);
	}

	/**
	 * @param nId
	 * @return
	 * @throws SQLException
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
	 * @throws SQLException
	 */
	public void writePage(LcdPage page) throws SQLException {
		pageConverter.selectToNewPage(page);
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	public void selectPage(int nId) throws SQLException {
		nSelectedSite = nId;
		writePage(getPageFromDB(nId));
	}

	/**
	 * @param sId
	 */
	public void selectPage(String sId) {
		try {
			selectPage(Integer.valueOf(sId));
		} catch (Exception e) {
			LOGGER.warn(sId + " ist keine SeitenId");
		}

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
		try {
			selectPage(nSelectedSite + 1);
		} catch (SQLException e) {
			// Seite nicht möglich
			this.nSelectedSite--;
			try {
				selectPage(nSelectedSite);
			} catch (SQLException e1) {
				LOGGER.warn("nextPage fail");
			}
		}

	}

	/**
	 * 
	 */
	public void lastPage() {
		try {
			selectPage(nSelectedSite - 1);
		} catch (SQLException e) {
			this.nSelectedSite++;
			// Nicht möglich
			try {
				selectPage(nSelectedSite);
			} catch (SQLException e1) {
				LOGGER.warn("lastPage fail");
			}
		}
	}

}
