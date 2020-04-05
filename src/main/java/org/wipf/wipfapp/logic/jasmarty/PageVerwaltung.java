package org.wipf.wipfapp.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.LcdPage;
import org.wipf.wipfapp.logic.base.App;
import org.wipf.wipfapp.logic.base.MsqlLite;

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
	public void initDB() throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS pages (pid INTEGER primary key autoincrement, name TEXT, page TEXT, options INTEGER);");
	}

	public void writeDefaultPage() {
		try {
			LcdPage p = new LcdPage();
			p.setLine(0, "jaSmarty");
			p.setLine(1, "by Wipf");
			p.setLine(2, "");
			p.setLine(3, "Version:" + App.VERSION);
			writePage(p);

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
	public void newPageToDB(String sName, String sPage, int nOptions) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute(
				"INSERT INTO pages (name, page, options) VALUES ('" + sName + "','" + sPage + "','" + nOptions + "')");
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
