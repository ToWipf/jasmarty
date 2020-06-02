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
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS customChars (id INTEGER UNIQUE, name TEXT, position INTEGER, data TEXT);");
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
			cc.setPosition(rs.getInt("position"));
			cc.setData(rs.getString("data"));
			return cc;
		} catch (Exception e) {
			return new CustomChar();
		}
	}

	/**
	 * @param conf
	 * @throws SQLException
	 */
	public void saveToDB(CustomChar cc) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO customChars (id, name, position, data) VALUES ('" + cc.getId() + "','"
				+ cc.getName() + "','" + cc.getPosition() + "','" + cc.getData() + "')");
	}

	/**
	 * @param jnRoot
	 */
	public void setCustomChar(String jnRoot) {
		try {
			saveToDB(new CustomChar().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("setCustomChar fehler");
		}
	}

	/**
	 * @param nIndexDB
	 * @param nIndexLcd
	 */
	public void loadCharToLcdFromDB(int nIndexDB) {
		lcdConnect.writeCustomChar(getFromDB(nIndexDB));
	}

	/**
	 * @param cc
	 */
	public void loadCharToLcd(CustomChar cc) {
		lcdConnect.writeCustomChar(cc);
	}

}
