package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.FilmEntry;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppFilme {

	private static final Logger LOGGER = Logger.getLogger("Telegram Filme");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		Statement stmt = SqlLite.getDB();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS filme (id INTEGER UNIQUE, titel TEXT, art TEXT, gesehendate INTEGER, infotext TEXT, bewertung INTEGER, editby TEXT, date INTEGER);");
		stmt.close();
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
			return getAllAsJson().toString();
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
	public List<FilmEntry> getAll() throws SQLException {
		List<FilmEntry> liTodoE = new ArrayList<>();

		Statement stmt = SqlLite.getDB();
		ResultSet rs = stmt.executeQuery("select * from filme;");

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
		stmt.close();
		return liTodoE;
	}

	/**
	 * @param tE
	 * @return id
	 */
	public Integer saveItem(FilmEntry tE) {
		try {
			Statement stmt = SqlLite.getDB();
			//@formatter:off
			stmt.execute("INSERT OR REPLACE INTO filme (id, titel, art, gesehendate, infotext, bewertung, editby, date) VALUES " +
					"('"  + tE.getId() +
					"','" + tE.getTitel() +
					"','" + tE.getArt() +
					"','" + tE.getGesehenDate() +
					"','" + tE.getInfotext() +
					"','" + tE.getBewertung() +
					"','" + tE.getEditBy() +
					"','" + tE.getDate() +
					"')");
			//@formatter:on
			stmt.close();
			return tE.getId();
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
		int nId = genNextId();
		FilmEntry te = new FilmEntry();
		te.setTitel(t.getMessageFullWithoutFirstWord());
		te.setEditBy(t.getFromIdOnly().toString());
		te.setDate(t.getDate());

		te.setId(nId);
		saveItem(te);

		return nId;
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
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM filme WHERE id LIKE '" + nId + "';");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("del todo " + e);
		}
	}

	/**
	 * @return
	 */
	private int genNextId() {
		int nNextId = 0;
		try {
			for (FilmEntry fe : this.getAll()) {
				if (fe.getId() > nNextId) {
					nNextId = fe.getId();
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getNextId " + e);
		}
		return nNextId + 1;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private Integer countItems() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM filme;");
			int n = rs.getInt("COUNT(*)");
			stmt.close();
			return n;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private String delByID(Integer nID) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM filme WHERE id = " + nID);
			stmt.close();
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete todo" + e);
			return "Fehler";
		}
	}

}
