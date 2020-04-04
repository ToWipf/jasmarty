package org.wipf.wipfapp.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.wipfapp.datatypes.LcdPage;
import org.wipf.wipfapp.logic.base.MsqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class PageVerwaltung {

	@Inject
	PageConverter pageConverter;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS pages (pid INTEGER primary key autoincrement, name TEXT, page TEXT);");
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void newPageToDB(LcdPage page) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute(
				"INSERT INTO pages (name, page) VALUES ('" + page.getName() + "','" + page.getPageAsDBString() + "')");
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
	public LcdPage getPageFromDB(int nId) throws SQLException {
		LcdPage page = new LcdPage(20, 4);

		Statement stmt = MsqlLite.getDB();
		ResultSet rs = stmt.executeQuery("SELECT * FROM pages WHERE pid = '" + nId + "';");
		page.setId(rs.getInt("pid"));
		page.setName(rs.getString("name"));
		page.stringToPage(rs.getString("page"));
		return page;
	}

	/**
	 * @param page
	 * @throws SQLException
	 */
	public void writePage(LcdPage page) throws SQLException {
		pageConverter.convertPage(page);
	}

	public void test() {
		try {
			LcdPage p = new LcdPage(20, 4);

			p.setName("testpage");
			p.setLine(0, "Line!1!".toCharArray());
			p.setLine(1, "    HIER".toCharArray());
			p.setLine(2, "ZEILE 3".toCharArray());
			p.setLine(3, "Ende der Page !!!".toCharArray());

			System.out.println("Schreibe:");
			System.out.println(p.getPageAsDBString());

			newPageToDB(p);

			System.out.println("Lese:");
			LcdPage pg = getPageFromDB(1);
			System.out.println(pg.getPageAsDBString());

			writePage(pg);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
