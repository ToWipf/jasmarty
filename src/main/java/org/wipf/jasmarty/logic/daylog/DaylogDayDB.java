package org.wipf.jasmarty.logic.daylog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
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
	 * @param o
	 * @throws SQLException
	 */
	public void save(DaylogDay o) throws SQLException {
		if (o.getId() != null) {
			// id vorhanden (update)
			String sUpdate = "INSERT OR REPLACE INTO daylogDay (id, date, tagestext, userid) VALUES (?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setString(2, o.getDate());
			statement.setString(3, o.getTagestext());
			statement.setInt(4, o.getUserId());
			statement.executeUpdate();
		} else {
			// neue id (insert)
			String sUpdate = "INSERT OR REPLACE INTO daylogDay (date, tagestext, userid) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, o.getDate());
			statement.setString(2, o.getTagestext());
			statement.setInt(3, o.getUserId());
			statement.executeUpdate();
		}
	}

	/**
	 * @param jnRoot
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		try {
			save(new DaylogDay().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get Day und erstelle ihn, wenn er noch nicht angelegt ist
	 * 
	 * @param sDate
	 * @return
	 * @throws SQLException
	 */
	public DaylogDay getDateAndCrateIfDateStringNotExists(String sDate, String sTagestext) throws SQLException {
		// TODO USERID nicht auf 0 setzen
		DaylogDay dday = get(sDate, 0);
		if (dday.getId() != null) {
			// Tag existiert bereits
			return dday;
		} else {
			// Tag erstellen
			DaylogDay newDay = new DaylogDay();
			newDay.setUserId(0);
			newDay.setDate(sDate);
			newDay.setTagestext(sTagestext);
			save(newDay);

			// Nochmals nach der Id suchen, sollte jetzt da sein
			return get(sDate, 0);
		}
	}

	/**
	 * 
	 * Get Day und erstelle ihn, wenn er noch nicht angelegt ist
	 * 
	 * Die Date Info wird immer aktualisiert
	 * 
	 * @param sDate
	 * @return
	 * @throws SQLException
	 */
	public DaylogDay getDateAndCrateIfDateNotExistsByJSON(String jnRoot) throws SQLException {
		DaylogDay ddayToCreate = new DaylogDay().setByJson(jnRoot);
		// Wenn eine ID dabei ist, immer aktualisieren
		if (ddayToCreate.getId() != null) {
			// Nur aktualisieren, Id ist mit dabei
			save(ddayToCreate);
			return ddayToCreate;
		} else {
			// Keine Id -> Prüfen ob datum existiert
			DaylogDay dayIfExists = get(ddayToCreate.getDate(), ddayToCreate.getUserId());

			if (dayIfExists != null) {
				// Tag existiert bereits -> Id übertagen
				ddayToCreate.setId(dayIfExists.getId());

				// Wenn im neuen Tag kein Tagestext übergeben wird -> übertagen
				if (ddayToCreate.getTagestext() == null || ddayToCreate.getTagestext().length() == 0) {
					ddayToCreate.setTagestext(dayIfExists.getTagestext());
				}

			}
			// Tag immer speichern, da das Date Info aktualisiert wird

			save(ddayToCreate);

		}

		// Nochmals nach der Id suchen, die jetzt da ist und Date zurückgeben
		return get(ddayToCreate.getDate(), ddayToCreate.getUserId());
	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public DaylogDay get(String sDate, Integer nUserId) throws SQLException {
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
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<DaylogDay> getAll(Integer nUserId) throws SQLException {
		List<DaylogDay> o = new LinkedList<>();

		String sQuery = "SELECT * FROM daylogDay WHERE userid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nUserId);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			DaylogDay d = new DaylogDay();
			d.setId(rs.getInt("id"));
			d.setDate(rs.getString("date"));
			d.setTagestext(rs.getString("tagestext"));
			d.setUserId(rs.getInt("userid"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson(Integer nUserId) throws SQLException {
		List<DaylogDay> l = getAll(nUserId);
		JSONArray ja = new JSONArray();
		for (DaylogDay d : l) {
			ja.put(d.toJson());
		}
		return ja;
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
