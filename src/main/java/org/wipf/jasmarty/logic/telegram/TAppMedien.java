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
import org.wipf.jasmarty.datatypes.telegram.MedienEntry;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMedien {

	@Inject
	SqlLite sqlLite;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram Medien");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS medien (id INTEGER primary key autoincrement UNIQUE, titel TEXT, art TEXT, typ TEXT, gesehendate INTEGER, infotext TEXT, bewertung INTEGER, editby TEXT, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueMedien(Telegram t) {
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

		for (MedienEntry tItem : getAll()) {
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
			for (MedienEntry tItem : getAll()) {
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
	public List<MedienEntry> getAll() {
		try {
			List<MedienEntry> liTodoE = new ArrayList<>();

			String sQuery = "select * from medien;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				MedienEntry entry = new MedienEntry();
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
	 * @param medienE
	 * @return id
	 */
	public Integer saveItem(MedienEntry medienE) {
		try {
			if (medienE.getId() != null) {
				// update
				String sUpdate = "INSERT OR REPLACE INTO medien (id, titel, art, gesehendate, infotext, bewertung, editby, date) VALUES (?,?,?,?,?,?,?,?)";

				PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
				statement.setInt(1, medienE.getId());
				statement.setString(2, medienE.getTitel());
				statement.setString(3, medienE.getArt());
				statement.setInt(4, medienE.getGesehenDate());
				statement.setString(5, medienE.getInfotext());
				statement.setInt(6, medienE.getBewertung());
				statement.setString(7, medienE.getEditBy());
				statement.setInt(8, medienE.getDate());
				statement.executeUpdate();

				return medienE.getId();
			} else {
				// insert
				String sUpdate = "INSERT INTO medien (titel, art, gesehendate, infotext, bewertung, editby, date) VALUES (?,?,?,?,?,?,?)";

				PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
				statement.setString(1, medienE.getTitel());
				statement.setString(2, medienE.getArt());
				statement.setInt(3, medienE.getGesehenDate());
				statement.setString(4, medienE.getInfotext());
				statement.setInt(5, medienE.getBewertung());
				statement.setString(6, medienE.getEditBy());
				statement.setInt(7, medienE.getDate());
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
		MedienEntry medienE = new MedienEntry();
		medienE.setTitel(t.getMessageFullWithoutFirstWord());
		medienE.setEditBy(t.getFromIdOnly().toString());
		medienE.setDate(t.getDate());
		medienE.setGesehenDate(0);
		medienE.setBewertung(0);

		return saveItem(medienE);
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public Integer saveItem(String jnRoot) {
		return saveItem(new MedienEntry().setByJson(jnRoot));
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
