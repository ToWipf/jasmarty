package org.wipf.jasmarty.logic.telegram.messageEdit;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
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

	private static final Logger LOGGER = Logger.getLogger("Telegram Msg");

	/**
	 * 
	 */
	public void initDB() {
		try {
			Statement stmt = SqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemsg (id integer primary key autoincrement, request TEXT, response TEXT, options TEXT, editby TEXT, date INTEGER);");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
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
	 * @param t
	 * @param nStelle
	 * @return
	 * 
	 *         TODO del this
	 */
	public Telegram getMsg(Telegram t, Integer nStelle) {
		try {
			Map<String, String> mapS = new HashMap<>();

			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("select * from telemsg where request = '" + t.getMessageStringPartLow(nStelle) + "';");
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
					+ t.getMessageStringPartLow(1) + "','" + t.getMessageFullWithoutSecondWordLow() + "','" + null + "','"
					+ t.getFrom() + "','" + t.getDate() + "')");
			stmt.close();
			return "OK: " + t.getMessageStringPartLow(1);
		} catch (Exception e) {
			LOGGER.warn("add telemsg " + e);
			return "Fehler";
		}

	}
}
