package org.wipf.jasmarty.logic.telegram;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MsgLog {

	private static final Logger LOGGER = Logger.getLogger("Telegram Log");

	/**
	 * @param t
	 */
	public void saveTelegramToLog(Telegram t) {
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
	 * @return log
	 */
	public String getTelegramLog(String sFilter) {
		try {
			StringBuilder slog = new StringBuilder();
			int n = 0;
			Statement stmt = SqlLite.getDB();
			// ResultSet rs = stmt.executeQuery("SELECT * FROM telegrambot WHERE msgid = '"
			// + nID + "';");
			ResultSet rs = stmt.executeQuery("SELECT * FROM telegramlog WHERE msgid IS NOT '0' ORDER BY msgdate ASC"); // DESC

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
			LOGGER.warn("getTelegram" + e);
			return "FAIL";
		}
	}

	/**
	 * @return
	 */
	public String contSend() {
		try {
			Statement stmt = SqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telegramlog;");
			stmt.close();
			return rs.getString("COUNT(*)") + " Nachrichten gesendet";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return "Fehler contSend";
		}
	}

}
