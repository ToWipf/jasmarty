package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.Telegram;
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
		sqlLite.getNewDb().prepareStatement(sUpdate).executeUpdate();
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

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemsg;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("request") + "\n");
			}
			stmt.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param sSQLFilter
	 * @return
	 */
	public List<Telegram> getAllMsg(String sSQLFilter) {
		List<Telegram> tList = new ArrayList<>();

		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM telemsg " + sSQLFilter);

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

			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("getAllMsg" + e);
		}
		return tList;
	}

	/**
	 * @return
	 */
	public JSONArray getMsgAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getAllMsg("")) {
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

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemsg where request = '"
					+ wipf.escapeStringSatzzeichen(t.getMessageStringPartLow(0)) + "';");
			while (rs.next()) {
				mapS.put(rs.getString("response"), rs.getString("options"));
			}
			rs.close();
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
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("get telemsg " + e);
		}
		return t;
	}

	/**
	 * @param t
	 * @return
	 */
	public String delMsg(Telegram t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("DELETE FROM telemsg WHERE id = " + t.getMessageIntPart(1));
			stmt.close();
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
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemsg;");
			String s = rs.getString("COUNT(*)") + " Antworten in der DB";
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler countMsg";
		}
	}

	/**
	 * @param t
	 */
	public String addMsg(Telegram t) {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemsg (request, response, options, editby, date) VALUES " + "('"
					+ t.getMessageStringPartLow(1) + "','" + t.getMessageFullWithoutSecondWord() + "','" + null + "','"
					+ t.getFrom() + "','" + t.getDate() + "')");
			stmt.close();
			return "OK: " + t.getMessageStringPartLow(1);
		} catch (Exception e) {
			LOGGER.warn("add telemsg " + e);
			return "Fehler";
		}

	}
}
