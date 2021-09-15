package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMsg {

	@Inject
	Wipf wipf;
	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Msg");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS telemsg (id integer primary key autoincrement, request TEXT, response TEXT, options TEXT, editby TEXT, date INTEGER);";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 * 
	 *         TODO del this -> json
	 */
	public String getAllMsg() {
		try {
			StringBuilder sb = new StringBuilder();

			String sQuery = ("select * from telemsg;");
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("request") + "\n");
			}
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd " + e);
		}
		return "Fehler";
	}

	/**
	 * @return
	 */
	public List<Telegram> getAllMsgAsList() {
		List<Telegram> tList = new ArrayList<>();
		try {
			String sQuery = "SELECT * FROM telemsg";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();

			while (rs.next()) {
				Telegram t = new Telegram();
				t.setMid(rs.getInt("id"));
				t.setMessage(rs.getString("request"));
				t.setAntwort(rs.getString("response"));
				t.setFrom(rs.getString("editby"));
				t.setDate((int) rs.getLong("date"));
				t.setType(rs.getString("options")); // options TEXT; Derzeit nicht benutzt
				t.setChatID(0);

				tList.add(t);
			}

		} catch (Exception e) {
			LOGGER.warn("getAllMsg " + e);
		}
		return tList;
	}

	/**
	 * @return
	 */
	public JSONArray getMsgAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getAllMsgAsList()) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getMsgAsJson" + e);
		}
		return ja;
	}

	/**
	 * TODO del this
	 * 
	 * @param t
	 * @param nStelle
	 * @return
	 * 
	 */
	public Telegram getMsg(Telegram t) {
		try {
			Map<String, String> mapS = new HashMap<>();

			String sQuery = "select * from telemsg where request = ?;";

			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			statement.setString(1, wipf.escapeStringSatzzeichen(t.getMessageStringPartLow(0)));
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				mapS.put(rs.getString("response"), rs.getString("options"));
			}

			if (mapS.size() != 0) {
				int nZufallsElement = wipf.getRandomInt(mapS.size());
				int n = 0;
				for (Map.Entry<String, String> entry : mapS.entrySet()) {
					if (n == nZufallsElement) {
						t.setAntwort(entry.getKey());
						t.setOptions(entry.getValue());
					}
					n++;
				}
			}
		} catch (Exception e) {
			LOGGER.warn("get telemsg " + e);
		}
		return t;
	}

	/**
	 * @param t
	 * @return
	 */
	public String delItemByTelegram(Telegram t) {
		return delItem(t.getMessageIntPart(1));
	}

	/**
	 * @param id
	 * @return
	 */
	public String delItem(int nId) {
		try {
			String sUpdate = "DELETE FROM telemsg WHERE id = ?";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setInt(1, nId);
			statement.executeUpdate();

			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemsg" + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 * 
	 *         TODO del this
	 */
	public String countMsg() {
		try {
			String sQuery = "SELECT COUNT(*) FROM telemsg;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getString("COUNT(*)") + " Antworten in der DB";
			}
			return "Keine Msgs";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler countMsg";
		}
	}

	/**
	 * @param t
	 */
	public String saveItemByTelegram(Telegram t) {
		Telegram tMsg = new Telegram();
		tMsg.setMessage(t.getMessageStringPartLow(1));
		tMsg.setAntwort(t.getMessageFullWithoutSecondWord());
		tMsg.setFrom(t.getFrom());
		tMsg.setDate(t.getDate());
		return saveItem(tMsg);
	}

	/**
	 * @param t
	 * @return
	 */
	public String saveItem(Telegram t) {
		try {
			String sUpdate = "INSERT OR REPLACE INTO telemsg (id, request, response, options, editby, date) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);

			if (t.getMid() > 0) {
				statement.setInt(1, t.getMid());
			} else {
				statement.setString(1, null); // erstellt die id automatisch
			}
			statement.setString(2, t.getMessage().toLowerCase());
			statement.setString(3, t.getAntwort());
			statement.setString(4, null);
			statement.setString(5, t.getFrom());
			statement.setInt(6, t.getDate());
			statement.executeUpdate();

			return "OK: " + t.getMessage();
		} catch (Exception e) {
			LOGGER.warn("add telemsg " + e);
			return "Fehler";
		}
	}

	/**
	 * @param sJson
	 */
	public String saveMsg(String sJson) {
		return saveItem(new Telegram().setByJsonTelegram(sJson));
	}

}
