package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.Telegram;
import org.wipf.jasmarty.datatypes.TicTacToe;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppTicTacToe {

	@Inject
	SqlLite sqlLite;

	private static final Logger LOGGER = Logger.getLogger("Telegram TicTacToe");

	/**
	 * @throws SQLException
	 * 
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS tictactoe (chatid INTEGER UNIQUE, feld TEXT, msgdate INTEGER, type TEXT);";
		sqlLite.getNewDb().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param t
	 * @return
	 */
	public String input(Telegram t) {
		try {
			return tttPlay(t);

		} catch (Exception e) {
			return "Fehler im Spiel. Neustarten mit 'ttt neu'";
		}
	}

	/**
	 * @param sTelegramSetTo
	 * @return
	 */
	private String tttPlay(Telegram t) {
		TicTacToe ttt = loadTicTacToe(t.getChatID());
		String sAction = t.getMessageStringPartLow(1);
		if (sAction == null) {
			return "Anleitung mit TicTacToe help";
		}

		switch (sAction) {
		case "setzen":
		case "setze":
		case "set":
		case "se":
		case "s":
			String sHelpAuswertung;
			// spiel vorhanden
			if (ttt == null) {
				return "Es wurde noch kein Spiel gestartet"; // TODO autocreate new game ?
			}
			// auswertung
			sHelpAuswertung = helpAuswertung(ttt);
			if (sHelpAuswertung != null) {
				return sHelpAuswertung;
			}
			ttt.setByTelegram(t);
			// setze feld
			if (!ttt.setByNummer(t.getMessageIntPart(2), 'X')) {
				return "Feld konnte nicht gesetzt werden";
			} else {
				saveTicTacToe(ttt); // save game
			}
			// auswertung
			sHelpAuswertung = helpAuswertung(ttt);
			if (sHelpAuswertung != null) {
				return sHelpAuswertung;
			}
			// set cpu
			if (!ttt.cpuSetzen('O')) {
				return "CPU konnte nicht setzen";
			} else {
				saveTicTacToe(ttt); // save game
			}
			// auswertung
			sHelpAuswertung = helpAuswertung(ttt);
			if (sHelpAuswertung != null) {
				return sHelpAuswertung;
			}
			// Spielfeld ausgeben
			return ttt.tttToNiceString();
		case "new":
		case "neu":
		case "ne":
		case "n":
			Random zufall = new Random();
			ttt = new TicTacToe("FFFFFFFFF");
			ttt.setByTelegram(t);
			saveTicTacToe(ttt);
			if (zufall.nextBoolean()) {
				ttt.cpuSetzen('O');
				saveTicTacToe(ttt);
			}
			return "Setzen mit 'ttt set NR'\n\n" + ttt.tttToNiceString();
		case "show":
		case "sh":
			return ttt.tttToNiceString();
		case "raw":
		case "ra":
		case "r":
			return ttt.tttToString();
		case "cpu":
			ttt.setByTelegram(t);
			if (!ttt.cpuSetzen('O')) {
				return "CPU konnte nicht setzen";
			} else {
				saveTicTacToe(ttt); // save game
				return ttt.tttToNiceString();
			}
		case "setonly":
			// setze feld ohne cpu
			ttt.setByTelegram(t);
			if (!ttt.setByNummer(t.getMessageIntPart(2), 'X')) {
				return "Feld konnte nicht gesetzt werden";
			} else {
				saveTicTacToe(ttt); // save game
				return ttt.tttToNiceString();
			}
		default:
			return "Anleitung:\n\nttt neu: Neues Spiel\nttt setze NR: Setzen\nttt show: Zeige Feld\nttt raw: Zeige Feld";
		}
	}

	/**
	 * @param ttt
	 * @return
	 */
	private String helpAuswertung(TicTacToe ttt) {
		Character win = ttt.auswertung();
		if (win != null) {
			if (win == 'U') {
				return "Unentschieden\n\n" + ttt.tttToNiceString() + "\n\n neues Spiel mit 'ttt neu'";
			} else if (win == 'X') {
				return "Du hast gewonnen\n\n" + ttt.tttToNiceString() + "\n\n neues Spiel mit 'ttt neu'";
			} else if (win == 'O') {
				return "Du hast verloren\n\n" + ttt.tttToNiceString() + "\n\n neues Spiel mit 'ttt neu'";
			}
		}
		return null;
	}

	/**
	 * @param ttt
	 * @return
	 */
	private Boolean saveTicTacToe(TicTacToe ttt) {
		try {
			String sUpdate = "INSERT OR REPLACE INTO tictactoe (chatid, feld, msgdate, type) VALUES (?,?,?,?)";

			PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sUpdate);
			statement.setInt(1, ttt.getChatID());
			statement.setString(2, ttt.getFieldString());
			statement.setInt(3, ttt.getDate());
			statement.setString(4, ttt.getType());
			statement.executeUpdate();

			return true;
		} catch (Exception e) {
			LOGGER.warn("setTicTacToe " + e);
			return false;
		}
	}

	/**
	 * @param sChatid
	 * @return
	 */
	private TicTacToe loadTicTacToe(Integer nChatid) {
		try {
			String sQuery = "SELECT * FROM tictactoe WHERE chatid = ?;";
			PreparedStatement statement = sqlLite.getNewDb().prepareStatement(sQuery);
			statement.setInt(1, nChatid);
			ResultSet rs = sqlLite.getNewDb().prepareStatement(sQuery).executeQuery();
			while (rs.next()) {
				// Es gibt nur einen oder keinen Treffer
				TicTacToe ttt = new TicTacToe(rs.getString("feld"));
				// ttt.setChatID(rs.getInt("chatid")); weitere felder sind nicht nötig -> werden
				// neu befüllt
				return ttt;
			}
		} catch (Exception e) {
			// Kann vorkommen wenn kein spiel aktiv ist
			// MLogger.warn("getTicTacToe " + e);
		}
		return null;
	}
}