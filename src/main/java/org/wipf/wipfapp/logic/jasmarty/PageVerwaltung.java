package org.wipf.wipfapp.logic.jasmarty;

import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.RequestScoped;

import org.wipf.wipfapp.datatypes.LcdPage;
import org.wipf.wipfapp.logic.base.MsqlLite;

@RequestScoped
public class PageVerwaltung {

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
		stmt.execute("INSERT INTO pages (name, page) VALUES ('" + page.getName() + "','" + page.getPage() + "')");
	}

	/**
	 * @param nPid
	 * @throws SQLException
	 */
	public void delPageFromDB(Integer nPid) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("DELETE FROM pages WHERE id = " + nPid);
	}

	public void getPageFromDB() {

	}

	public void selectPageFromDB() {

	}

	public void test() {
		LcdPage p = new LcdPage();
		char[][] page = new char[20][4];
		page[0] = "TestPageInhalt".toCharArray();
		page[1] = "LineZwei".toCharArray();
		page[2] = "Line3".toCharArray();
		page[3] = "EndeDerTestSeite".toCharArray();

		p.setName("testpage");
		p.setPage(page);

		try {
			newPageToDB(p);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
