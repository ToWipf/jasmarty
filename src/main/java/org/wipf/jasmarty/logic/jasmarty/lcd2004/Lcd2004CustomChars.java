package org.wipf.jasmarty.logic.jasmarty.lcd2004;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.jasmarty.CustomChar;
import org.wipf.jasmarty.logic.base.SqlLite;

@ApplicationScoped
public class Lcd2004CustomChars {

	@Inject
	Lcd2004 lcd2004;
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("LcdCustomChars");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS customChars (id INTEGER UNIQUE, name TEXT, position INTEGER, data TEXT);";
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @return
	 */
	public CustomChar getFromDB(int nId) {
		CustomChar cc = new CustomChar();
		try {
			String sQuery = "SELECT * FROM customChars WHERE id = ?;";
			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sQuery);
			statement.setInt(1, nId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// Es gibt nur einen oder keinen Eintrag
				cc.setId(rs.getInt("id"));
				cc.setName(rs.getString("name"));
				cc.setPosition(rs.getInt("position"));
				cc.setData(rs.getString("data"));
				return cc;
			}
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
		String sUpdate = "INSERT OR REPLACE INTO customChars (id, name, position, data) VALUES (?,?,?,?)";

		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, cc.getId());
		statement.setString(2, cc.getName());
		statement.setInt(3, cc.getPosition());
		statement.setString(4, cc.getData());
		statement.executeUpdate();
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
		if (lcd2004.isLcdOk()) {
			this.lcd2004.setbPauseWriteToLCD(true);
			lcd2004.writeCustomChar(cc);
			this.lcd2004.setbPauseWriteToLCD(false);
		}
	}

	/**
	 * @param cca
	 */
	public void loadCharToLcd(CustomChar[] cca) {
		if (lcd2004.isLcdOk()) {
			this.lcd2004.setbPauseWriteToLCD(true);
			for (CustomChar cc : cca) {
				lcd2004.writeCustomChar(cc);
			}
			this.lcd2004.setbPauseWriteToLCD(false);
		}
	}

}
