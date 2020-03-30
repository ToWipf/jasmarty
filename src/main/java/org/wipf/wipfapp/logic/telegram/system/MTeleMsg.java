package org.wipf.wipfapp.logic.telegram.system;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.Telegram;
import org.wipf.wipfapp.logic.base.MBlowfish;
import org.wipf.wipfapp.logic.base.MWipf;
import org.wipf.wipfapp.logic.base.MsqlLite;
import org.wipf.wipfapp.logic.base.Wipfapp;
import org.wipf.wipfapp.logic.telegram.apps.MEssen;
import org.wipf.wipfapp.logic.telegram.apps.MMumel;
import org.wipf.wipfapp.logic.telegram.apps.MOthers;
import org.wipf.wipfapp.logic.telegram.apps.MTicTacToe;
import org.wipf.wipfapp.logic.telegram.apps.MTodoList;

/**
 * @author wipf
 *
 */
public class MTeleMsg {

	private static final Logger LOGGER = Logger.getLogger("MTeleMsg");

	/**
	 * 
	 */
	public static void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemsg (id integer primary key autoincrement, request TEXT, response TEXT, options TEXT, editby TEXT, date INTEGER);");
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS telemotd (id integer primary key autoincrement, text TEXT, editby TEXT, date INTEGER);");
		} catch (Exception e) {
			LOGGER.warn("initDB telemsg " + e);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public static String menueMsg(Telegram t) {
		// Admin Befehle
		if (MTelegram.isAdminUser(t)) {
			switch (t.getMessageStringPart(0)) {
			case "admin":
				// @formatter:off
				return 
					"Admin Befehle:" + "\n\n" + 
					"AddAMsgToDB" + "\n" + 
					"GetAllMsg" + "\n" +
					"GetAllMotd" + "\n" +
					"AddAMotd" + "\n" +
					"SendMotd" + "\n" + 
					"SendInfo" + "\n" +
					"GetMotd" + "\n" + 
					"DelMotd ID" +  "\n" +
					"DelMsg ID"+  "\n" +
					"DoPing IP" + "\n" +
					"DoNmap" + "\n" +
					"getIp" + "\n" +
					"SendIp" + "\n" +
					"send ID msg" + "\n" +
					"Essen (Hilfe für essen)";
				// @formatter:on

			// Anbindung an msg datenbank
			case "addamsgtodb":
				return addMsg(t);
			case "getallmsg":
				return getAllMsg();
			case "delmsg":
				return delMsg(t);

			// Anbindung an motd datenbank
			case "addamotd":
				return addMotd(t);
			case "getallmotd":
				return getAllMotd();
			case "delmotd":
				return delMotd(t);
			case "getmotd":
				return MTeleMsg.getMotd();

			// Auto Msg Tests
			case "sendmotd":
				sendDaylyMotd();
				return "OK";
			case "sendinfo":
				sendDaylyInfo();
				return "OK";

			case "doping":
				return MWipf.ping(t.getMessageStringRawPart(1)).toString();
			case "shell":
				return MWipf.shell(t.getMessageStringFirst());

			case "send":
				return sendToId(t);

			case "donmap":
				return MWipf.escapeString(MWipf.donmap());
			case "getip":
				return MWipf.escapeString(MWipf.getExternalIp());
			case "sendip": {
				sendExtIp();
				return "OK";
			}

			default:
				break;
			}
		}
		// Alle festen Antworten
		switch (t.getMessageStringPart(0)) {
		case "start":
			return "Wipfbot Version:" + Wipfapp.VERSION + "\nInfos per 'info'";
		case "v":
		case "ver":
		case "version":
		case "info":
		case "about":
		case "hlp":
		case "hilfe":
		case "help":
		case "wipfbot":
			return "Wipfbot\nVersion " + Wipfapp.VERSION + "\nCreated by Tobias Fritsch\nwipf2@web.de";
		case "r":
		case "rnd":
		case "zufall":
			return MOthers.zufall(t.getMessageStringPart(1), t.getMessageStringPart(2));
		case "c":
		case "cr":
		case "en":
		case "encrypt":
			return MBlowfish.encrypt(t.getMessageStringFirst(), Wipfapp.sKey);
		case "d":
		case "de":
		case "dc":
		case "decrypt":
			return MBlowfish.decrypt(t.getMessageStringFirst(), Wipfapp.sKey);
		case "t":
		case "ttt":
		case "tictactoe":
		case "play":
		case "game":
			return MTicTacToe.input(t);
		case "time":
		case "date":
		case "datum":
		case "uhr":
		case "zeit":
		case "clock":
		case "z":
			return MWipf.dateTime();
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return MOthers.getWitz();
		case "m":
		case "mummel":
		case "mumel":
		case "ml":
			return MMumel.playMumel(t);
		case "countmsg":
			return MTeleMsg.countMsg();
		case "countsend":
			return MTelegram.contSend();
		case "telestats":
			return MWipf.dateTime() + "\n" + MTeleMsg.countMsg() + "\n" + MTelegram.contSend();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: "
					+ t.getMid();
		case "essen":
		case "e":
			return MEssen.menueEssen(t);
		case "to":
		case "todo":
			return MTodoList.menueTodoList(t);
		default:
			// Alle db aktionen
			t = getMsg(t, 0);
			// ob keine Antwort in db gefunden
			if (t.getAntwort() != null) {
				return t.getAntwort();
			} else {
				switch (MWipf.getRandomInt(4)) {
				case 0:
					return "Keine Antwort vorhanden";
				case 1:
					return "Leider ist keine Antwort vorhanden";
				case 2:
					return "Hierzu gibt es keine Antwort";
				default:
					return "Antwort nicht vorhanden";
				}
			}
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private static String delMsg(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM telemsg WHERE id = " + t.getMessageIntPart(1));
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemsg" + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private static String delMotd(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM telemotd WHERE id = " + t.getMessageIntPart(1));
			return "DEL";
		} catch (Exception e) {
			LOGGER.warn("delete telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 */
	private static String addMsg(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemsg (request, response, options, editby, date) VALUES " + "('"
					+ t.getMessageStringPart(1) + "','" + t.getMessageStringSecond() + "','" + null + "','"
					+ t.getFrom() + "','" + t.getDate() + "')");
			return "OK: " + t.getMessageStringPart(1);
		} catch (Exception e) {
			LOGGER.warn("add telemsg " + e);
			return "Fehler";
		}

	}

	/**
	 * @param t
	 */
	private static String addMotd(Telegram t) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO telemotd (text, editby, date) VALUES " + "('"
					+ t.getMessageStringFirst() + "','" + t.getFrom() + "','" + t.getDate() + "')");
			return "IN";
		} catch (Exception e) {
			LOGGER.warn("add telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @return
	 */
	private static String countMsg() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemsg;");
			return rs.getString("COUNT(*)") + " Antworten in der DB";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	private static String countMotd() {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM telemotd;");
			return rs.getString("COUNT(*)") + " Motds in der DB";
		} catch (Exception e) {
			LOGGER.warn("count Telegram " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	private static String getMotd() {
		try {
			String s = null;

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd ORDER BY RANDOM() LIMIT 1");
			while (rs.next()) {
				// Es gibt nur einen Eintrag
				s = (rs.getString("text"));
			}
			rs.close();
			// return "Nachricht des Tages\n " + MTime.date() + "\n\n" + s;
			return s;

		} catch (Exception e) {
			LOGGER.warn("get telemotd " + e);
			return "Fehler";
		}
	}

	/**
	 * @param t
	 * @param nStelle
	 * @return
	 */
	private static Telegram getMsg(Telegram t, Integer nStelle) {
		try {
			Map<String, String> mapS = new HashMap<>();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("select * from telemsg where request = '" + t.getMessageStringPart(nStelle) + "';");
			while (rs.next()) {
				mapS.put(rs.getString("response"), rs.getString("options"));
			}
			rs.close();
			if (mapS.size() != 0) {
				int nZufallsElement = MWipf.getRandomInt(mapS.size());
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
	private static String getAllMotd() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemotd;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("text") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 */
	private static String getAllMsg() {
		try {
			StringBuilder sb = new StringBuilder();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from telemsg;");
			while (rs.next()) {
				sb.append(rs.getString("id") + "\t");
				sb.append(rs.getString("request") + "\n");
			}
			rs.close();
			return sb.toString();

		} catch (Exception e) {
			LOGGER.warn("get all telemotd" + e);
		}
		return "Fehler";
	}

	/**
	 * @param t
	 * @return
	 */
	private static String sendToId(Telegram t) {
		Telegram tSend = new Telegram();
		tSend.setChatID(t.getMessageIntPart(1));
		tSend.setAntwort(t.getMessageStringSecond());
		tSend.setType("from: " + t.getChatID());

		MTelegram.saveTelegramToDB(tSend);
		MTelegram.sendToTelegram(tSend);
		return "done";
	}

	/**
	 * 
	 */
	public static void sendDaylyInfo() {
		Telegram t = new Telegram();
		t.setAntwort(MWipf.dateTimeMs() + "\n" + MTeleMsg.countMsg() + "\n" + MTeleMsg.countMotd() + "\n"
				+ MTelegram.contSend() + "\n\nVersion:" + Wipfapp.VERSION);
		t.setChatID(798200105);

		MTelegram.saveTelegramToDB(t);
		MTelegram.sendToTelegram(t);
	}

	/**
	 * 
	 */
	public static void sendDaylyMotd() {
		Telegram t = new Telegram();
		t.setAntwort(MTeleMsg.getMotd());
		t.setChatID(-387871959);

		MTelegram.saveTelegramToDB(t);
		MTelegram.sendToTelegram(t);
	}

	/**
	 * @param sMsg
	 */
	public static Boolean sendMsgToGroup(String sMsg) {
		Telegram t = new Telegram();
		t.setAntwort(sMsg);
		t.setChatID(-387871959);

		MTelegram.saveTelegramToDB(t);
		MTelegram.sendToTelegram(t);
		return true;
	}

	/**
	 * 
	 */
	public static void sendExtIp() {
		Telegram t = new Telegram();
		t.setAntwort("Neue IP: " + MWipf.getExternalIp());
		t.setChatID(798200105);

		MTelegram.saveTelegramToDB(t);
		MTelegram.sendToTelegram(t);
	}

}
