package org.wipf.jasmarty.logic.telegram;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.databasetypes.telegram.Usercache;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.datatypes.telegram.TicTacToe;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppTicTacToe {

	@Inject
	TUsercache tUsercache;

	/**
	 * @param t
	 * @return
	 */
	public String input(Telegram t) {
		try {
			return tttPlay(t);

		} catch (Exception e) {
			e.printStackTrace();
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
	private void saveTicTacToe(TicTacToe ttt) {
		Usercache uc = tUsercache.get(ttt.getChatID());
		uc.chatid = ttt.getChatID();
		uc.usercache = ttt.getFieldString();
		uc.msg = ttt.getMessage();
		uc.saveOrUpdate();
	}

	/**
	 * @param sChatid
	 * @return
	 */
	private TicTacToe loadTicTacToe(Long nChatid) {
		Usercache uc = tUsercache.get(nChatid);
		if (uc.usercache != null && uc.usercache.length() == 9) {
			return new TicTacToe(uc.usercache);
		}
		// Kein Cache -> neues Spiel
		return new TicTacToe("FFFFFFFFF");
	}
}