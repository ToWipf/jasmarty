package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.CustomChar;
import org.wipf.jasmarty.logic.base.SqlLite;

@ApplicationScoped
public class CustomChars {

	@Inject
	LcdConnect lcdConnect;

	private static final Logger LOGGER = Logger.getLogger("LcdCustomChars");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS customChars (id INTEGER UNIQUE, name TEXT, position INTEGER, data TEXT);");
	}

	/**
	 * @return
	 */
	public CustomChar getFromDB(int nId) {
		CustomChar cc = new CustomChar();
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM customChars WHERE id = '" + nId + "';");
			cc.setId(rs.getInt("id"));
			cc.setName(rs.getString("name"));
			cc.setPosition(rs.getInt("position"));
			cc.setData(rs.getString("data"));
			stmt.close();
		} catch (SQLException e) {
			LOGGER.warn("getFromDB " + e);
		}
		return cc;
	}

	/**
	 * @param conf
	 * @throws SQLException
	 */
	public void saveToDB(CustomChar cc) throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO customChars (id, name, position, data) VALUES ('" + cc.getId() + "','"
				+ cc.getName() + "','" + cc.getPosition() + "','" + cc.getData() + "')");
		stmt.close();
	}

	/**
	 * @param jnRoot
	 * @throws SQLException
	 */
	public void setCustomChar(String jnRoot) throws SQLException {
		saveToDB(new CustomChar().setByJson(jnRoot));
	}

	/**
	 * @param nIndexDB
	 * @param nIndexLcd
	 */
	public void loadCharToLcdFromDB(int nIndexDB) {
		this.loadCharToLcd(this.getFromDB(nIndexDB));
	}

	/**
	 * @param naIndexDB
	 */
	public void loadCharToLcdFromDB(int[] naIndexDB) {
		ArrayList<CustomChar> listCustomChar = new ArrayList<CustomChar>();
		for (int id : naIndexDB) {
			listCustomChar.add(this.getFromDB(id));
		}
		this.loadCharToLcd(listCustomChar.toArray(new CustomChar[listCustomChar.size()]));
	}

	/**
	 * @param cc
	 */
	public void loadCharToLcd(CustomChar cc) {
		if (lcdConnect.isLcdOk()) {
			this.lcdConnect.setbPauseWriteToLCD(true);
			lcdConnect.writeCustomChar(cc);
			this.lcdConnect.setbPauseWriteToLCD(false);
		}
	}

	/**
	 * @param cca
	 */
	public void loadCharToLcd(CustomChar[] cca) {
		if (lcdConnect.isLcdOk()) {
			this.lcdConnect.setbPauseWriteToLCD(true);
			for (CustomChar cc : cca) {
				lcdConnect.writeCustomChar(cc);
			}
			this.lcdConnect.setbPauseWriteToLCD(false);
		}
	}

}
