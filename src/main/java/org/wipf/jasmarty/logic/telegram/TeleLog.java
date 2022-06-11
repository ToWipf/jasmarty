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
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TeleLog {

	@Inject
	SqlLite sqlLite;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	public enum sqlFilterSelect {
		ALL, EXTERN
	};

	/**
	 * 
	 */
	public void initDB() {
		try {
			String sUpdate = "CREATE TABLE IF NOT EXISTS telegramlog (msgid INTEGER, msg TEXT, antw TEXT, chatid INTEGER, msgfrom TEXT, msgdate INTEGER, type TEXT);";
			sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param t
	 */
	public void saveToLog(Telegram t) {
		if (t.getMid() == 0 && t.getType() == null) {
			t.setMid(-1);
			t.setType("system");
		}

		String sUpdate = ("INSERT INTO telegramlog (msgid, msg, antw, chatid, msgfrom, msgdate, type) VALUES (?,?,?,?,?,?,?)");

		try (PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate)) {
			statement.setInt(1, t.getMid());
			statement.setString(2, t.getMessage().replaceAll("'", "_"));
			statement.setString(3, t.getAntwort().replaceAll("'", "_"));
			statement.setLong(4, t.getChatID());
			statement.setString(5, t.getFrom());
			statement.setInt(6, t.getDate());
			statement.setString(7, t.getType());
			statement.executeUpdate();

		} catch (Exception e) {
			LOGGER.warn("____Log failed:");
			LOGGER.warn("id  : " + t.getMid());
			LOGGER.warn("msg : " + t.getMessage());
			LOGGER.warn("antw: " + t.getAntwort());
			LOGGER.warn("from: " + t.getFrom());
			LOGGER.warn("saveTelegramToLog " + e);
		}
	}

	/**
	 * @param nId
	 * @return
	 */
	public String delItem(Long nId) {
		LOGGER.info("del log id:" + nId);
		try {
			String sUpdate = "DELETE FROM telegramlog WHERE msgid LIKE ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
			statement.setLong(1, nId);
			statement.executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("del log " + e);
			return "FAIL";
		}
		return "OK";
	}

	/**
	 * @return
	 */
	public String countMsg() {
		try {
			String sQuery = "SELECT COUNT(*) FROM telegramlog;";
			ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				return rs.getString("COUNT(*)") + " Nachrichten im Log";
			}
			return "Fehler count log";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler count";
		}
	}

	/**
	 * @param sSQLFilter
	 * @return
	 */
	public List<Telegram> getTelegramLog(sqlFilterSelect filter) {
		String sSQLFilter = "";
		switch (filter) {
		case ALL:
			sSQLFilter = "";
			break;
		case EXTERN:
			sSQLFilter = "WHERE msgid IS NOT '0' AND type IS NOT 'system' AND chatid IS NOT '798200105' AND chatid IS NOT '-385659721' AND chatid IS NOT '522467648' AND chatid IS NOT '-387871959'";
		default:
			break;
		}

		List<Telegram> tList = new ArrayList<>();

		String sQuery = "SELECT * FROM telegramlog " + sSQLFilter;
		try (ResultSet rs = sqlLite.getDbApp().prepareStatement(sQuery).executeQuery()) {

			while (rs.next()) {
				Telegram t = new Telegram();
				t.setMid(rs.getInt("msgid"));
				t.setMessage(rs.getString("msg"));
				t.setAntwort(rs.getString("antw"));
				t.setChatID(rs.getLong("chatid"));
				t.setFrom(rs.getString("msgfrom"));
				t.setDate((int) rs.getLong("msgdate"));
				t.setType(rs.getString("type"));

				tList.add(t);
			}

		} catch (Exception e) {
			LOGGER.warn("getTelegram " + e);
		}
		return tList;
	}

	/**
	 * @return
	 */
	public JSONArray getTelegramLogAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getTelegramLog(sqlFilterSelect.ALL)) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson " + e);
		}
		return ja;
	}

	/**
	 * @return
	 */
	public JSONArray getTelegramLogAsJsonEXTERN() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getTelegramLog(sqlFilterSelect.EXTERN)) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson " + e);
		}
		return ja;
	}

	/**
	 * 
	 */
	public void cleanLog() {
		LOGGER.info("clean Logs");

		List<Long> lIds = new ArrayList<>();
		lIds.add(798200105l);
		lIds.add(-385659721l);
		lIds.add(-387871959l);
		lIds.add(-387712260l);
		lIds.add(0l);
		lIds.add(-1l);

		lIds.forEach((nCid) -> {

			String sUpdate = "DELETE FROM telegramlog WHERE chatid LIKE ?;";
			try (PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate)) {
				statement.setLong(1, nCid);
				statement.executeUpdate();
			} catch (Exception e) {
				LOGGER.warn("clean log " + e);
			}
		});

		// Systemmeldungen löschen
		delItem(-1l);
	}

	/**
	 * Eine Id auflößen
	 * 
	 * @param sId
	 */
	public String infoZuId(String sId) {
		String s = infoZuIdSql(sId);
		if (s != null) {
			return wipf.escapeStringSaveCode(wipf.jsonToStringAsList(new JSONObject(s)));
		}
		return "nicht gefunden";
	}

	/**
	 * Eine Id auflößen
	 * 
	 * @param sId
	 * @return
	 * @throws SQLException
	 */
	private String infoZuIdSql(String sId) {
		try {

			String sQuery = "SELECT * FROM telegramlog WHERE msgfrom LIKE ?;";
			PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
			statement.setString(1, "%" + sId + "%");
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// Das erst besste zurückgeben
				return (rs.getString("msgfrom"));
			}
		} catch (Exception e) {
			LOGGER.warn("infoZuId " + e);
			e.printStackTrace();
		}

		return null;
	}

}
