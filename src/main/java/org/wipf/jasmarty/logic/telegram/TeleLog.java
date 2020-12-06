package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			sqlLite.getDbJasmarty().prepareStatement(sUpdate).executeUpdate();
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
			String sUpdate = ("INSERT INTO telegramlog (msgid, msg, antw, chatid, msgfrom, msgdate, type) VALUES (?,?,?,?,?,?,?)");

			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sUpdate);
			statement.setInt(1, t.getMid());
			statement.setString(2, t.getMessage().replaceAll("'", "_"));
			statement.setString(3, t.getAntwort().replaceAll("'", "_"));
			statement.setInt(4, t.getChatID());
			statement.setString(5, t.getFrom());
			statement.setInt(6, t.getDate());
			statement.setString(7, t.getType());
			statement.executeUpdate();

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

			String sQuery = "SELECT * FROM telegramlog WHERE msgid IS NOT '0' AND type IS NOT 'system' ORDER BY msgdate ASC"; // DESC
			PreparedStatement statement = sqlLite.getDbJasmarty().prepareStatement(sQuery);
			ResultSet rs = statement.executeQuery();

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

			String sQuery = "SELECT * FROM telegramlog WHERE msgid IS NOT '0' AND type IS NOT 'system' AND chatid IS NOT 79820010 ORDER BY msgdate ASC";
			ResultSet rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();

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
			String sQuery = "SELECT COUNT(*) FROM telegramlog;";
			ResultSet rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				return rs.getString("COUNT(*)") + " Nachrichten gesendet";
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
	public List<Telegram> getTelegramLog(String sSQLFilterSelect) {
		String sSQLFilter = "";
		switch (sSQLFilterSelect) {
		case "full":
			sSQLFilter = "WHERE msgid IS NOT '0' AND type IS NOT 'system'";
			break;
		case "extern":
			sSQLFilter = "WHERE msgid IS NOT '0' AND type IS NOT 'system' AND chatid IS NOT '798200105' AND chatid IS NOT '-385659721' AND chatid IS NOT '522467648' AND chatid IS NOT '-387871959'";
		default:
			break;
		}

		List<Telegram> tList = new ArrayList<>();

		try {
			String sQuery = "SELECT * FROM telegramlog " + sSQLFilter;
			ResultSet rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();

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
			for (Telegram t : getTelegramLog("full")) {
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
			for (Telegram t : getTelegramLog("extern")) {
				ja.put(t.toJson());
			}
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson " + e);
		}
		return ja;
	}

}
