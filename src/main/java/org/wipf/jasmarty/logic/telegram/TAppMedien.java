package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.FilmEntry;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppFilm {

	@Inject
	SqlLite sqlLite;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram Filme");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		System.out.println("UPDATING HIER");
		// PATCH
		sqlLite.getDbApp().prepareStatement(
				"CREATE TABLE medien (id INTEGER primary key autoincrement UNIQUE, titel TEXT, art TEXT, typ TEXT, gesehendate INTEGER, infotext TEXT, bewertung INTEGER, editby TEXT, date INTEGER);")
				.executeUpdate();

		sqlLite.getDbApp().prepareStatement(
				"INSERT INTO medien(id, titel, art, gesehendate, infotext, bewertung, editby, date) SELECT id, titel, art, gesehendate, infotext, bewertung, editby, date FROM filme;")
				.executeUpdate();

		// String sUpdate = "CREATE TABLE IF NOT EXISTS medien (id INTEGER primary key
		// autoincrement UNIQUE, titel TEXT, art TEXT, typ TEXT, gesehendate INTEGER,
		// infotext TEXT, bewertung INTEGER, editby TEXT, date INTEGER);";
		// sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueFilme(Telegram t) {
		String sAction = t.getMessageStringPartLow(1);
		if (sAction == null) {
			// @formatter:off
				return 
					"text (add) titel" + "\n" + 
					"delete ID" + "\n" +
					"list" +  "\n" +
					"count";
			// @formatter:on
		}

		switch (sAction) {
		case "del":
		case "delete":
			return delByID(t.getMessageIntPart(2));
		case "l":
		case "list":
			return getAllAsList();
		case "c":
		case "count":
			return countItems().toString();
		default:
			return saveItem(t).toString();
		}
	}

	/**
	 * @return
	 */
	public String getAllAsList() {
		StringBuilder sb = new StringBuilder();

		for (FilmEntry tItem : getAll()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}

			sb.append(tItem.getTitel() + "\n");
			sb.append(tItem.getInfotext() + "\n");
			sb.append(tItem.getDate() + "\n");
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * @return
	 */
	public JSONArray getAllAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (FilmEntry tItem : getAll()) {
				ja.put(tItem.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson" + e);
		}
		return ja;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List<FilmEntry> getAll() {
		try {
			List<FilmEntry> liTodoE = new ArrayList<>();

			String sQuery = "select * from medien;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				FilmEntry entry = new FilmEntry();
				entry.setId(rs.getInt("id"));
				entry.setArt(rs.getString("art"));
				entry.setEditBy(rs.getString("editby"));
				entry.setTitel(rs.getString("titel"));
				entry.setInfotext(rs.getString("infotext"));
				entry.setGesehenDate(rs.getInt("gesehendate"));
				entry.setBewertung(rs.getInt("bewertung"));
				entry.setDate(rs.getInt("date"));
				liTodoE.add(entry);
			}
			return liTodoE;
		} catch (Exception e) {
			LOGGER.warn("getAll " + e);
			return null;
		}
	}

	/**
	 * @param filmE
	 * @return id
	 */
	public Integer saveItem(FilmEntry filmE) {
		try {
			if (filmE.getId() != null) {
				// update
				String sUpdate = "INSERT OR REPLACE INTO medien (id, titel, art, gesehendate, infotext, bewertung, editby, date) VALUES (?,?,?,?,?,?,?,?)";

				PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
				statement.setInt(1, filmE.getId());
				statement.setString(2, filmE.getTitel());
				statement.setString(3, filmE.getArt());
				statement.setInt(4, filmE.getGesehenDate());
				statement.setString(5, filmE.getInfotext());
				statement.setInt(6, filmE.getBewertung());
				statement.setString(7, filmE.getEditBy());
				statement.setInt(8, filmE.getDate());
				statement.executeUpdate();

				return filmE.getId();
			} else {
				// insert
				String sUpdate = "INSERT INTO medien (titel, art, gesehendate, infotext, bewertung, editby, date) VALUES (?,?,?,?,?,?,?)";

				PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
				statement.setString(1, filmE.getTitel());
				statement.setString(2, filmE.getArt());
				statement.setInt(3, filmE.getGesehenDate());
				statement.setString(4, filmE.getInfotext());
				statement.setInt(5, filmE.getBewertung());
				statement.setString(6, filmE.getEditBy());
				statement.setInt(7, filmE.getDate());
				statement.executeUpdate();

				return -999; // TODO echte ID holen
			}
		} catch (Exception e) {
			LOGGER.warn("save " + e);
			return null;
		}
	}

	/**
	 * hier kann nur der titel gesetzt werden
	 * 
	 * @param t
	 * @return
	 */
	private Integer saveItem(Telegram t) {
		FilmEntry filmE = new FilmEntry();
		filmE.setTitel(t.getMessageFullWithoutFirstWord());
		filmE.setEditBy(t.getFromIdOnly().toString());
		filmE.setDate(t.getDate());
		filmE.setGesehenDate(0);
		filmE.setBewertung(0);

		return saveItem(filmE);
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public Integer saveItem(String jnRoot) {
		return saveItem(new FilmEntry().setByJson(jnRoot));
	}

	/**
	 * @param nId
	 */
	public void deleteItem(Integer nId) {
		try {
			String sUpdate = "DELETE FROM medien WHERE id LIKE ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, nId);
			statement.executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("del todo " + e);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private Integer countItems() {
		try {
			String sQuery = "SELECT COUNT(*) FROM medien;";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {

				int n = rs.getInt("COUNT(*)");
				return n;
			}
			return -1;
		} catch (Exception e) {
			return -2;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String delByID(Integer nID) {
		try {
			String sUpdate = "DELETE FROM medien WHERE id = " + nID;
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, nID);
			statement.executeUpdate();

			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

}
