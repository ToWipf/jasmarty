package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TeleLog {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	/**
	 * 
	 */
	public void initDB() {
		try {
			String sUpdate = "CREATE TABLE IF NOT EXISTS telegramlog (msgid INTEGER, msg TEXT, antw TEXT, chatid INTEGER, msgfrom TEXT, msgdate INTEGER, type TEXT);";
			sqlLite.getNewDb().prepareStatement(sUpdate).executeUpdate();
		} catch (Exception e) {
			LOGGER.warn("initDB  " + e);
		}
	}

	/**
	 * @param t
	 */
	public void saveTelegramToLog(Telegram t) {
		if (t.getMid() == 0 && t.getType() == null) {
			t.setMid(-1);
			t.setType("system");
		}

		try {
			Statement stmt = SqlLite.getDB();
			stmt.execute("INSERT INTO telegramlog (msgid, msg, antw, chatid, msgfrom, msgdate, type)" + " VALUES ('"
					+ t.getMid() + "','" + t.getMessage().replaceAll("'", "_") + "','"
					+ t.getAntwort().replaceAll("'", "_") + "','" + t.getChatID() + "','" + t.getFrom() + "','"
					+ t.getDate() + "','" + t.getType() + "')");
			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("id  : " + t.getMid());
			LOGGER.warn("msg : " + t.getMessage());
			LOGGER.warn("antw: " + t.getAntwort());
			LOGGER.warn("from: " + t.getFrom());
			LOGGER.warn("saveTelegramToLog " + e);
		}
	}

	/**
	 * TODO Löschen
	 * 
	 * @return log
	 */
	@Deprecated
	public String genTelegramLog(String sFilter) {
		// TODO filter ins sql!
		try {
			StringBuilder slog = new StringBuilder();
			int n = 0;
			Statement stmt = SqlLite.getDB();
			// ResultSet rs = stmt.executeQuery("SELECT * FROM telegrambot WHERE msgid = '"
			// + nID + "';");
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM telegramlog WHERE msgid IS NOT '0' AND type IS NOT 'system' ORDER BY msgdate ASC"); // DESC

			while (rs.next()) {
				n++;
				Date date = new Date(rs.getLong("msgdate") * 1000);
				StringBuilder sb = new StringBuilder();

				if (sFilter == null || !rs.getString("msgfrom").contains(sFilter)) {
					sb.append(n + ":\n");
					sb.append("msgid:  \t" + rs.getString("msgid") + "\n");
					sb.append("msg in: \t" + rs.getString("msg") + "\n");
					sb.append("msg out:\t" + rs.getString("antw") + "\n");
					sb.append("chatid: \t" + rs.getString("chatid") + "\n");
					sb.append("msgfrom:\t" + rs.getString("msgfrom") + "\n");
					sb.append("msgdate:\t" + date + "\n");
					sb.append("type:   \t" + rs.getString("type") + "\n");
					sb.append("----------------\n\n");
					slog.insert(0, sb);
				}
			}
			stmt.close();
			return slog.toString();
		} catch (Exception e) {
			LOGGER.warn("genTelegram" + e);
			return "FAIL";
		}
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public String genTelegramLogUnknown() {
		try {
			StringBuilder slog = new StringBuilder();
			int n = 0;
			Statement stmt = SqlLite.getDB();

			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM telegramlog WHERE msgid IS NOT '0' AND type IS NOT 'system' AND chatid IS NOT 79820010 ORDER BY msgdate ASC");

			while (rs.next()) {
				n++;
				Date date = new Date(rs.getLong("msgdate") * 1000);
				StringBuilder sb = new StringBuilder();

				sb.append(n + ":\n");
				sb.append("msgid:  \t" + rs.getString("msgid") + "\n");
				sb.append("msg in: \t" + rs.getString("msg") + "\n");
				sb.append("msg out:\t" + rs.getString("antw") + "\n");
				sb.append("chatid: \t" + rs.getString("chatid") + "\n");
				sb.append("msgfrom:\t" + rs.getString("msgfrom") + "\n");
				sb.append("msgdate:\t" + date + "\n");
				sb.append("type:   \t" + rs.getString("type") + "\n");
				sb.append("----------------\n\n");
				slog.insert(0, sb);

			}
			stmt.close();
			return slog.toString();
		} catch (Exception e) {
			LOGGER.warn("genTelegram" + e);
			return "FAIL";
		}
	}

	/**
	 * @return
	 */
	public String count() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telegramlog;");
			String s = rs.getString("COUNT(*)") + " Nachrichten gesendet";
			stmt.close();
			return s;
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler count";
		}
	}

	/**
	 * @param sSQLFilter
	 * @return
	 */
	public List<Telegram> getTelegramLog(String sSQLFilter) {
		List<Telegram> tList = new ArrayList<>();

		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM telegramlog " + sSQLFilter);

			while (rs.next()) {
				Telegram t = new Telegram();
				t.setMid(rs.getInt("msgid"));
				t.setMessage(rs.getString("msg"));
				t.setAntwort(rs.getString("antw"));
				t.setChatID(rs.getInt("chatid"));
				t.setFrom(rs.getString("msgfrom"));
				t.setDate((int) rs.getLong("msgdate"));
				t.setType(rs.getString("type"));

				tList.add(t);
			}

			stmt.close();
		} catch (Exception e) {
			LOGGER.warn("getTelegram" + e);
		}
		return tList;
	}

	/**
	 * @return
	 */
	public JSONArray getTelegramLogAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getTelegramLog("WHERE msgid IS NOT '0' AND type IS NOT 'system'")) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson" + e);
		}
		return ja;
	}

	/**
	 * @return
	 */
	public JSONArray getTelegramLogAsJsonEXTERN() {
		JSONArray ja = new JSONArray();
		try {
			for (Telegram t : getTelegramLog(
					"WHERE msgid IS NOT '0' AND type IS NOT 'system' AND chatid IS NOT '798200105' AND chatid IS NOT '-385659721' AND chatid IS NOT '522467648' AND chatid IS NOT '-387871959'")) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson" + e);
		}
		return ja;
	}

}
