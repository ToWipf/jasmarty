package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.CustomChar;
import org.wipf.jasmarty.logic.base.MsqlLite;

@ApplicationScoped
public class CustomChars {

	private static final Logger LOGGER = Logger.getLogger("LcdCustomChars");

	@Inject
	LcdConnect lcdConnect;

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS customChars (id INTEGER UNIQUE, name TEXT,"
					+ " part0 INTEGER, part1 INTEGER, part2 INTEGER, part3 INTEGER, part4 INTEGER, part5 INTEGER, part6 INTEGER, part7 INTEGER);");
		} catch (Exception e) {
			LOGGER.error("init DB");
		}
	}

	/**
	 * @return
	 */
	public CustomChar getFromDB(int nId) {
		try {
			CustomChar cc = new CustomChar();
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM customChars WHERE id = '" + nId + "';");
			cc.setId(rs.getInt("id"));
			cc.setName(rs.getString("name"));
			cc.setLine(0, rs.getInt("part0"));
			cc.setLine(1, rs.getInt("part1"));
			cc.setLine(2, rs.getInt("part2"));
			cc.setLine(3, rs.getInt("part3"));
			cc.setLine(4, rs.getInt("part4"));
			cc.setLine(5, rs.getInt("part5"));
			cc.setLine(6, rs.getInt("part6"));
			cc.setLine(7, rs.getInt("part7"));
			return cc;
		} catch (Exception e) {
			return new CustomChar();
		}
	}

	/**
	 * @param conf
	 * @throws SQLException
	 */
	private void saveToDB(CustomChar cc) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute(
				"INSERT OR REPLACE INTO customChars (id, name, part0, part1, part2, part3, part4, part5, part6, part7) VALUES ('"
						+ cc.getId() + "','" + cc.getName() + "','" + cc.getLine(0) + "','" + cc.getLine(1) + "','"
						+ cc.getLine(2) + "','" + cc.getLine(3) + "','" + cc.getLine(4) + "','" + cc.getLine(5) + "','"
						+ cc.getLine(6) + "','" + cc.getLine(7) + "')");
	}

	/**
	 * @param jnRoot
	 */
	public void setCustomChar(String jnRoot) {
		try {
			saveToDB(new CustomChar().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}

	/**
	 * @param cc
	 * @param nIndex
	 */
	private void loadCharToLcd(CustomChar cc, int nIndex) {
		lcdConnect.writeCustomChar(cc, nIndex);
	}

	/**
	 * @param nIndexDB
	 * @param nIndexLcd
	 */
	private void loadCharToLcdFromDB(int nIndexDB, int nIndexLcd) {
		loadCharToLcd(getFromDB(nIndexDB), nIndexLcd);
	}

	// DELETE THIS TODO
	public void test() {
		CustomChar xx = new CustomChar();
		xx.setName("test");
		xx.setId(1);
		xx.setLine(0, 32);
		xx.setLine(1, 31);
		xx.setLine(2, 30);
		xx.setLine(3, 29);
		xx.setLine(4, 28);
		xx.setLine(5, 27);
		xx.setLine(6, 26);
		xx.setLine(7, 25);
		try {
			saveToDB(xx);
			loadCharToLcdFromDB(1, 4);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
