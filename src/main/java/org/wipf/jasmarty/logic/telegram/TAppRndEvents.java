package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.RndEvent;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppRndEvents {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram rndEvent");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS rndEvent (id INTEGER primary key autoincrement, eventtext TEXT, aktiv INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String menueRndEvent(Telegram t) {
		try {
			String sAction = t.getMessageStringPartLow(1);
			if (sAction == null) {
				return "Anleitung mit rndEvent Hilfe";
			}

			switch (sAction) {
			case "add":
				return save(t);
			case "del":
				return del(t.getMessageIntPart(1));
//			case "list":
//				return getAllRndEvent();
			case "count":
				return count();
			case "get":
				return getRndEventRnd();
			default:
				return
			//@formatter:off
					"rndEvent Add: rndEvent hinzufügen\n" +
					"rndEvent Del: id löschen\n" + 
//					"rndEvent List: alles auflisten\n" +
					"rndEvent Get: ZufallsrndEvent\n" +
					"rndEvent Count: Anzahl der Einträge\n";
			//@formatter:on
			}
		} catch (SQLException e) {
			LOGGER.error("menueRndEvent");
			e.printStackTrace();
			return "Fehler 018: " + e;
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public String getRndEventRnd() throws SQLException {
		String sQuery = "select eventtext from rndEvent ORDER BY RANDOM() WHERE aktiv = 1 LIMIT 1;";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			// Es gibt nur einen Eintrag
			return (rs.getString("eventtext"));
		}
		return null;
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	private String save(Telegram t) throws SQLException {
		RndEvent re = new RndEvent();
		re.setEventText(t.getMessageFullWithoutFirstWord());
		re.setActive(true);
		return save(re);
	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		try {
			save(new RndEvent().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param o
	 * @return
	 * @throws SQLException
	 */
	public String save(RndEvent o) throws SQLException {
		if (o.getId() != null) {
			// update
			String sUpdate = "INSERT OR REPLACE INTO rndEvent (id, eventtext, active) VALUES (?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, o.getId());
			statement.setString(2, o.getEventText());
			statement.setBoolean(3, o.getActive());
			statement.executeUpdate();
		} else {
			// insert
			String sUpdate = "INSERT INTO rndEvent (eventtext, active) VALUES (?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setString(1, o.getEventText());
			statement.setBoolean(2, o.getActive());
			statement.executeUpdate();
		}

		return "gespeichert";
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private String count() throws SQLException {
		String sQuery = ("SELECT COUNT(*) FROM rndEvent;");
		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			return rs.getString("COUNT(*)") + " Einträge in der DB";
		}

		return "FAIL";
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM rndEvent WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
		return "DEL";
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public List<RndEvent> getAll() throws SQLException {
		List<RndEvent> lr = new LinkedList<>();
		String sQuery = "select * from rndEvent;";

		ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
		while (rs.next()) {
			RndEvent re = new RndEvent();
			re.setId(rs.getInt("id"));
			re.setEventText(rs.getString("eventtext"));
			re.setActive(rs.getBoolean("active"));
			lr.add(re);
		}
		return lr;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<RndEvent> l = getAll();
		JSONArray ja = new JSONArray();
		for (RndEvent d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
