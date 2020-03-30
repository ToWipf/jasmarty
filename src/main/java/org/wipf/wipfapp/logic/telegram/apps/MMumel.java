package org.wipf.wipfapp.logic.telegram.apps;

import java.sql.ResultSet;
import java.sql.Statement;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.datatypes.MumelSpiel;
import org.wipf.wipfapp.datatypes.MumelSpieler;
import org.wipf.wipfapp.datatypes.Telegram;
import org.wipf.wipfapp.logic.base.MsqlLite;

/**
 * @author wipf
 *
 */
public class MMumel {

	private static final Logger LOGGER = Logger.getLogger("MMumel");

	/**
	 * 
	 */
	public static void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS mumelSpieler (" + " chatid INTEGER," + " playerid INTEGER,"
					+ " leben INTEGER," + " feuer INTEGER," + " blitz INTEGER," + " wasser INTEGER,"
					+ " doppelfeuer INTEGER," + " doppelwasser INTEGER," + " schwarzerstein INTEGER," + " geld INTEGER,"
					+ " hausbrennen INTEGER," + " bunterstein INTEGER, UNIQUE(chatid,playerid));");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS mumelSpiel (" + " chatid INTEGER UNIQUE," + " playerids TEXT"
					+ " weristdran INTEGER, date TEXT);");

		} catch (Exception e) {
			LOGGER.warn("initDB mumel " + e);
		}
	}

	/**
	 * @return
	 */
	public static String playMumel(Telegram t) {
		try {
			if ("NOT".equals("IN USE")) {
				saveGame(null);
				loadGame(null);

				MumelSpieler mspl = new MumelSpieler();
				mspl.setByTelegram(t);
				mspl.newFill();
				savePlayer(mspl);
			}

		} catch (Exception e) {
			LOGGER.warn("play Mumel " + e);
		}
		return "mumel is in build";
	}

	/**
	 * @param ttt
	 * @return
	 */
	private static Boolean saveGame(MumelSpiel m) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO mumelSpiel (chatid, playerids, weristdran, date) VALUES " + "('"
					+ m.getChatID() + "','" + m.getMumelSpielerliste() + "','" + m.getWerIstDran() + "','" + m.getDate()
					+ "')");
			return true;
		} catch (Exception e) {
			LOGGER.warn("savegame mumel " + e);
			return false;
		}
	}

	/**
	 * @param mspl
	 * @return
	 */
	private static Boolean savePlayer(MumelSpieler mspl) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("INSERT OR REPLACE INTO mumelSpieler "
					+ "(chatid, playerid, leben, feuer, blitz, wasser, doppelfeuer, doppelwasser, schwarzerstein, geld,  hausbrennen, bunterstein )"
					+ " VALUES " + "('" + mspl.getChatID() + "','" + mspl.getPlayerId() + "','" + mspl.getLeben()
					+ "','" + mspl.getFeuer() + "','" + mspl.getBlitz() + "','" + mspl.getWasser() + "','"
					+ mspl.getDoppelFeuer() + "','" + mspl.getDoppelWasser() + "','" + mspl.getSchwarzerStein() + "','"
					+ mspl.getGeld() + "','" + mspl.getHausBrennen() + "','" + mspl.getBunterStein() + "')");
			return true;
		} catch (Exception e) {
			LOGGER.warn("mumel save " + e);
			return false;
		}
	}

	/**
	 * @param sChatid
	 * @return
	 */
	private static MumelSpiel loadGame(Integer nChatid) {
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tictactoe WHERE chatid = '" + nChatid + "';");
			MumelSpiel m = new MumelSpiel();

			m.setMumelSpielerliste(rs.getString("playerids"));
			// ttt.setChatID(rs.getInt("chatid")); weitere felder sind nicht nötig -> werden
			// neu befüllt
			rs.close();
			return m;
		} catch (Exception e) {
			// Kann vorkommen wenn kein spiel aktiv ist
			// MLogger.warn("loadGame " + e);
		}
		return null;
	}
}
