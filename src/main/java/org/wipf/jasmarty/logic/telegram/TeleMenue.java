package org.wipf.jasmarty.logic.telegram;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TeleMenue {

	@Inject
	Wipf wipf;
	@Inject
	TAppOthers appOthers;
	@Inject
	TAppTicTacToe appTicTacToe;
	@Inject
	TAppTodoList appTodoList;
	@Inject
	TAppEssen appEssen;
	@Inject
	TeleLog msglog;
	@Inject
	UserAndGroups userAndGroups;
	@Inject
	TAppMsg appMsg;
	@Inject
	TAppMotd appMotd;
	@Inject
	TAppFilm appFilme;

	/**
	 * @param sJson
	 * @return
	 */
	public String menueMsg(String sJson) {
		Telegram t = new Telegram().setByJsonTelegram(sJson);
		return menueMsg(t);
	}

	/**
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public String menueMsg(Telegram t) {
		String sInMsg = wipf.escapeStringSatzzeichen(t.getMessageStringPartLow(0));

		// Admin Befehle
		if (userAndGroups.isAdminUser(t)) {
			switch (sInMsg) {
			case "admin":
				// @formatter:off
				return "Admin Befehle:" + "\n\n" + 
					"AddAMsgToDB" + "\n" + 
					"GetAllMsg" + "\n" + 
					"GetAllMotd" + "\n"+ 
					"AddAMotd" + "\n" + 
					"SendMotd" + "\n" + 
					"telestats" + "\n" + 
					"GetMotd" + "\n" + 
					"DelMotd ID" + "\n" + 
					"DelMsg ID" + "\n" + 
					"DoPing IP" + "\n" + 
					"DoNmap" + "\n" + 
					"getIp" + "\n" + 
					"SendIp" + "\n" + 
					"send ID msg" + "\n" + 
					"online" + "\n" + 
				    "scan" + "\n" +
				    "response" + "\n" +
				    "filme" + "\n" +
				    "todo" + "\n" +
					"Essen (Hilfe für essen)";
				// @formatter:on

			// Anbindung an msg datenbank
			case "addamsgtodb":
				return appMsg.saveItemByTelegram(t);
			case "getallmsg":
				return appMsg.getAllMsg();
			case "delmsg":
				return appMsg.delItemByTelegram(t);

			// Anbindung an motd datenbank
			case "addamotd":
				return appMotd.addMotd(t);
			case "getallmotd":
				return appMotd.getAllMotd();
			case "delmotd":
				return appMotd.delMotd(t);
			case "getmotd":
				return appMotd.getRndMotd();

			case "doping":
				return wipf.ping(t.getMessageStringPartLow(1)).toString();

			case "getip":
				return wipf.getExternalIp();

			case "stats":
			case "telestats":
				return wipf.time("dd.MM.yyyy HH:mm:ss") + "\n" + appMsg.countMsg() + "\n" + msglog.count();

			case "res":
			case "response":
				return t.getMessage();

			case "to":
			case "todo":
				return appTodoList.telegramMenueTodoList(t);
			case "filme":
			case "f":
				return appFilme.telegramMenueFilme(t);
			case "essen":
			case "e":
				return appEssen.menueEssen(t);

			case "online":
			case "scan":
				return appOthers.getOnline();

			default:
				break;
			}
		}

		// Alle public Antworten
		switch (sInMsg) {
		case "start":
			return "Wipfbot Jasmarty Version:" + MainHome.VERSION + "\nInfos per 'info'";
		case "v":
		case "ver":
		case "version":
		case "info":
		case "about":
		case "hlp":
		case "hilfe":
		case "help":
		case "wipfbot":
			return "Wipfbot Jasmarty\nVersion " + MainHome.VERSION + "\nCreated by Tobias Fritsch\nwipf2@web.de";
		case "r":
		case "rnd":
		case "zufall":
			return appOthers.zufall(t.getMessageStringPartLow(1), t.getMessageStringPartLow(2));
		case "c":
		case "cr":
		case "en":
		case "encrypt":
			// TODO
			return wipf.encrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
		case "d":
		case "de":
		case "dc":
		case "decrypt":
			// TODO
			return wipf.decrypt(t.getMessageFullWithoutFirstWord(), "TODO_KEY");
		case "t":
		case "ttt":
		case "tictactoe":
		case "play":
		case "game":
			return appTicTacToe.input(t);
		case "time":
		case "date":
		case "datum":
		case "uhr":
		case "zeit":
		case "clock":
		case "z":
			return wipf.time("dd.MM.yyyy HH:mm:ss");
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return appOthers.getWitz();
		case "countmsg":
			return appMsg.countMsg();
		case "countsend":
			return msglog.count();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: "
					+ t.getMid();
		case "calc":
		case "math":
		case "m":
			return String.valueOf(wipf.doMathByString(t.getMessageFullWithoutFirstWord()));
		default:
			// Alle db aktionen
			t = appMsg.getMsg(t);
			// ob keine Antwort in db gefunden
			if (t.getAntwort() != null) {
				return t.getAntwort();
			} else {
				switch (wipf.getRandomInt(4)) {
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

}
