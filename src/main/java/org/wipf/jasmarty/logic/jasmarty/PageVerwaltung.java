package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.LcdPage;
import org.wipf.jasmarty.logic.base.App;
import org.wipf.jasmarty.logic.base.MsqlLite;

/**
 * @author wipf
 * 
 */
@RequestScoped
public class PageVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("PageVerwaltung");

	@Inject
	PageConverter pageConverter;

	/**
	 * @throws SQLException
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS pages (pid INTEGER primary key autoincrement, name TEXT, page TEXT, options TEXT);");
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
	public void newPageToDB(LcdPage page) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT INTO pages (name, page, options) VALUES ('" + page.getName() + "','"
				+ page.getPageAsDBString() + "','" + page.getOptions() + "')");
	}

	/**
	 * @param sName
	 * @param sPage
	 * @throws SQLException
	 */
	public void newPageToDB(String sName, String sPage, String sOptions) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute(
				"INSERT INTO pages (name, page, options) VALUES ('" + sName + "','" + sPage + "','" + sOptions + "')");
	}

	/**
	 * @param nPid
	 * @throws SQLException
	 */
	public void delPageFromDB(Integer nPid) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("DELETE FROM pages WHERE id = " + nPid);
	}

	/**
	 * @param nId
	 * @return
	 * @throws SQLException
	 */
	public LcdPage getPageFromDB(int nPid) {
		try {
			LcdPage page = new LcdPage();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pages WHERE pid = '" + nPid + "';");
			page.setId(rs.getInt("pid"));
			page.setName(rs.getString("name"));
			page.setStringToPage(rs.getString("page"));
			return page;
		} catch (Exception e) {
			LOGGER.warn("Page not found: " + nPid);
			return null;
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
	 * @param nPid
	 * @throws SQLException
	 */
	public void selectPage(int nPid) throws SQLException {
		writePage(getPageFromDB(nPid));
	}
}
