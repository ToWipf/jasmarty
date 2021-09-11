package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.daylog.DaylogDay;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogDayDB {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS daylogDay (id INTEGER NOT NULL UNIQUE, date TEXT, tagestext TEXT, userid INTEGER, PRIMARY KEY(id AUTOINCREMENT));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param day
	 * @throws SQLException
	 */
	public void save(DaylogDay day) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO daylogDay (id, date, tagestext, userid) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, day.getId());
		statement.setString(2, day.getDate());
		statement.setString(3, day.getTagestext());
		statement.setInt(4, day.getUserId());
		statement.executeUpdate();
	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public DaylogDay load(String sDate, Integer nUserId) throws SQLException {
		DaylogDay o = new DaylogDay();

		String sQuery = "SELECT * FROM daylogDay WHERE date = ? AND userid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setString(1, sDate);
		statement.setInt(2, nUserId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			// Es gibt nur einen oder keinen Eintrag
			o.setId(rs.getInt("id"));
			o.setDate(rs.getString("date"));
			o.setTagestext(rs.getString("tagestext"));
			o.setUserId(rs.getInt("userid"));
			return o;
		}
		// Datum nicht gefunden -> neu erstllen
		o.setDate(sDate);

		return o;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM daylogDay WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

}
