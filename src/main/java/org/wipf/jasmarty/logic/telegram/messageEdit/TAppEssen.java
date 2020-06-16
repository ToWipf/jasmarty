package org.wipf.jasmarty.logic.telegram.messageEdit;

//
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import javax.enterprise.context.ApplicationScoped;
//
//import org.jboss.logging.Logger;
//import org.wipf.jasmarty.datatypes.Telegram;
//import org.wipf.jasmarty.logic.base.SqlLite;
//
///**
// * @author wipf
// *
// */
//@ApplicationScoped
//public class TAppEssen {
//
//	private static final Logger LOGGER = Logger.getLogger("MEssen");
//
//	/**
//	 * 
//	 */
//	public static void initDB() {
//		try {
//			Statement stmt = SqlLite.getDB();
//			stmt.executeUpdate(
//					"CREATE TABLE IF NOT EXISTS essen (id integer primary key autoincrement, type TEXT, name TEXT, options TEXT, editby TEXT, date INTEGER);");
//		} catch (Exception e) {
//			LOGGER.warn("initDB essen " + e);
//		}
//	}
//
//	/**
//	 * 
//	 */
//	public static void sendDaylyEssen() {
//		Telegram t = new Telegram();
//		t.setAntwort("Vorschlag für heute:" + "\n" + getEssenRnd());
//		t.setChatID(-385659721);
//
//		MTelegram.saveTelegramToDB(t);
//		MTelegram.sendToTelegram(t);
//	}
//
//	/**
//	 * @param t
//	 * @return
//	 */
//	public static String menueEssen(Telegram t) {
//		String sAction = t.getMessageStringPart(1);
//		if (sAction == null) {
//			return "Anleitung mit Essen hilfe";
//		}
//
//		// Admin Befehle
//		if (MTelegram.isAdminUser(t)) {
//			switch (sAction) {
//			case "add":
//				return addEssen(t);
//			case "del":
//				return delEssen(t);
//			case "list":
//				return getAllEssen();
//			case "send":
//				sendDaylyEssen();
//				return "OK";
//			case "count":
//				return count();
//			}
//
//		}
//
//		// public Antworten
//		switch (sAction) {
//		case "get":
//			return getEssenRnd();
//		default:
//			return
//			//@formatter:off
//					"Essen Add: Essen hinzufügen\n" +
//					"Essen Del: id löschen\n" + 
//					"Essen List: alles auflisten\n" +
//					"Essen Get: Zufallsessen\n" +
//					"Essen Count: Anzahl der Einträge\n" +
//					"Essen Send: Zufallsessen senden\n";
//			//@formatter:on
//		}
//	}
//
//	/**
//	 * @param t
//	 * @return
//	 */
//	private static String addEssen(Telegram t) {
//		try {
//			Statement stmt = MsqlLite.getDB();
//			//@formatter:off
//			stmt.execute("INSERT OR REPLACE INTO essen (name, editby, date) VALUES " +
//					"('" + t.getMessageStringSecond() +
//					"','" + t.getFrom() +
//					"','"+ t.getDate() +
//					"')");
//			//@formatter:on
//			return "gespeichert";
//		} catch (Exception e) {
//			LOGGER.warn("add essen " + e);
//			return "Fehler";
//		}
//	}
//
//	/**
//	 * @return
//	 */
//	private static String count() {
//		try {
//			Statement stmt = MsqlLite.getDB();
//			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM essen;");
//			return rs.getString("COUNT(*)") + " Einträge in der DB";
//		} catch (Exception e) {
//			LOGGER.warn("count essen " + e);
//			return null;
//		}
//	}
//
//	/**
//	 * @param t
//	 * @return
//	 */
//	private static String delEssen(Telegram t) {
//		try {
//			Statement stmt = MsqlLite.getDB();
//			stmt.execute("DELETE FROM essen WHERE id = " + t.getMessageIntPart(2));
//			return "DEL";
//		} catch (Exception e) {
//			LOGGER.warn("delete essen" + e);
//			return "Fehler";
//		}
//	}
//
//	/**
//	 * @param t
//	 * @return
//	 */
//	private static String getAllEssen() {
//		try {
//			StringBuilder sb = new StringBuilder();
//
//			Statement stmt = MsqlLite.getDB();
//			ResultSet rs = stmt.executeQuery("select * from essen;");
//			while (rs.next()) {
//				sb.append(rs.getString("id") + "\t");
//				sb.append(rs.getString("name") + "\n");
//			}
//			rs.close();
//			return sb.toString();
//
//		} catch (Exception e) {
//			LOGGER.warn("get all essen" + e);
//		}
//		return "Fehler";
//	}
//
//	/**
//	 * @return
//	 */
//	private static String getEssenRnd() {
//		try {
//			String s = null;
//
//			Statement stmt = MsqlLite.getDB();
//			ResultSet rs = stmt.executeQuery("select * from essen ORDER BY RANDOM() LIMIT 1");
//			while (rs.next()) {
//				// Es gibt nur einen Eintrag
//				s = (rs.getString("name"));
//			}
//			rs.close();
//			// return "Nachricht des Tages\n " + MTime.date() + "\n\n" + s;
//			return s;
//
//		} catch (Exception e) {
//			LOGGER.warn("get essen rnd " + e);
//			return "Fehler";
//		}
//	}
//}
