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
		sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param day
	 * @throws SQLException
	 */
	public void save(DaylogDay day) throws SQLException {
		String sUpdate = "INSERT OR REPLACE INTO daylogDay (id, date, tagestext, userid) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
		statement.setInt(1, day.getId());
		statement.setString(2, day.getDate());
		statement.setString(3, day.getTagestext());
		statement.setInt(4, day.getUserId());
		statement.executeUpdate();
	}

	/**
	 * @return
	 * 
	 */
	public DaylogDay load(String sDate) { // TODO userid
		DaylogDay o = new DaylogDay();
		try {

			String sQuery = "SELECT * FROM daylogDay WHERE date = ?;";
			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sQuery);
			statement.setString(1, sDate);
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
		} catch (Exception e) {
			// TODO
		}
		return o;
	}
}
