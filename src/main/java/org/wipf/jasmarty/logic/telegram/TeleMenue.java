package org.wipf.jasmarty.logic.telegram;

import java.util.Date;

import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.FileVW;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.daylog.DaylogHome;
import org.wipf.jasmarty.logic.daylog.TAppDaylog;
import org.wipf.jasmarty.logic.discord.DiscordHome;
import org.wipf.jasmarty.logic.listen.ListeService;
import org.wipf.jasmarty.logic.listen.MedienService;
import org.wipf.jasmarty.logic.listen.RndEventsService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
	RndEventsService appRndEvent;
	@Inject
	TeleLogService telelog;
	@Inject
	TUserAndGroups userAndGroups;
	@Inject
	TAppMsgService appMsg;
	@Inject
	MedienService appMedien;
	@Inject
	TSendAndReceive sendAndReceive;
	@Inject
	TAppGrafana grafana;
	@Inject
	TAppDaylog appDaylog;
	@Inject
	TUsercache tUsercache;
	@Inject
	DaylogHome daylogHome;
	@Inject
	FileVW fileVw;
	@Inject
	DiscordHome discord;
	@Inject
	ListeService listedb;

	/**
	 * @param sJson
	 * @return
	 */
	public String menueMsgApi(String sJson) {
		Telegram t = new Telegram().setByJsonTelegram(sJson);
		String antwort = menueMsg(t);
		if (antwort != null)
			return antwort.replaceAll("\n", "\\\\n");
		return "LEERE ANTWORT";
	}

	/**
	 * @param t
	 * @return
	 */
	public String menueMsg(Telegram t) {
		String res = doMenue(t);
		tUsercache.saveByTelegramOhneUsercache(t);
		return res;
	}

	/**
	 * @param t
	 * @return
	 * 
	 */
	private String doMenue(Telegram t) {
		String sInMsg = wipf.escapeStringSatzzeichen(t.getBeginnStringFromMessage());

		// Beginnt mit . ignorieren
		if (sInMsg.startsWith(".")) {
			return null;
		}

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
				    "medien" + "\n" +
				    "todo" + "\n" +
					"rndEvent\n" +
					"punkte \n" +
					"pp"+ "\n" +
					"pm / mp\n" +
					"ns / nochSpiele\n" +
					"sns / setNochSpiele\n" +
					"setpunkte / sp / ps / mp\n" +
					"changepunkte / pc / pa N\n" +
					"itext TEXT IN DER APP\n" +
					"kill\n" +
					"system\n" +
					"temperature\n" +
					"infozuid\n" +
					"getfile\n" +
					"filelist\n" +
					"txt\n"+
					"dl dh di |daylog\n" +
					"backup\n" +
					"discord\n" +
					"discordlist\n" +
					"clearlog"
					;
				// @formatter:on

			// Anbindung an msg datenbank
//			case "addamsgtodb":
//				return appMsg.saveItemByTelegram(t);
//			case "getallmsg":
//				return appMsg.getAllMsg();
//			case "delmsg":
//				return appMsg.delItemByTelegram(t);

			// Cache und Log Stats
			case "stats":
			case "telestats":
			case "cache":
				return wipf.getTime("dd.MM.yyyy HH:mm:ss") + "\n" + appMsg.countMsg() + "\n" + telelog.countMsg() + "\n\n" + tUsercache.getAllAsText();
			case "res":
			case "return":
			case "response":
				return t.getMessage();
//			case "izi":
//			case "infozuid":
//				return telelog.infoZuId(t.getMessageFullWithoutFirstWord());

			// Listen
			case "to":
			case "todo":
				return listedb.addStringToList(t.getMessageFullWithoutFirstWord());
			case "to-link": // Spezielle funktion
				return listedb.addStringToList(t.getMessage());

			case "filme":
			case "f":
			case "m":
			case "medien":
				return appMedien.telegramMenueMedien(t);
			case "e":
			case "rndevent":
				return appRndEvent.menueRndEvent(t);

			// Web
			case "getip":
				return wipf.getExternalIp();
			case "doping":
				return wipf.ping(t.getMessageStringPartLow(1)).toString();

			// System
			case "kill":
				// Noch ein Update machen, ansonsen wird nach einen neustart sofort wieder
				// "kill" aufgerufen
				sendAndReceive.readUpdateFromTelegram();
				MainHome.stopApp();
				return "killed";

			case "d":
			case "dev":
				return grafana.telegramMenueDev(t);

			case "lt":
			case "langertext":
				return appOthers.langerText(t.getMessageIntPart(1));

			// Daylog
			case "dl":
			case "daylog":
				return appDaylog.telegramMenue(t);
			case "di":
			case "dayinfo":
				return daylogHome.getTagesinfoByTelegram(t);
			case "dt":
			case "daytype":
				return daylogHome.getAllUniqueEventTextByTyp(t);

			// Files
			case "gf":
			case "fg":
			case "getfile":
			case "fileget":
				return sendAndReceive.sendDocumentToTelegram(t);
			case "df":
			case "fl":
			case "file":
			case "files":
			case "filelist":
			case "fileliste":
				return fileVw.getFilesForTelegram();
			case "b":
			case "db":
			case "backup":
				return sendAndReceive.sendDatabaseToTelegram(t);

			case "stf":
			case "txt":
			case "stringtofile":
				return fileVw.telegramToFile(t);

			case "dis":
			case "discord":
			case "disl":
			case "discordlist":
			case "disn":
			case "discordnames":
				return discord.getUserAsString();

			case "cleanlog:":
			case "clearlog:":
			case "cl":
				telelog.cleanLog();
				return "CleanLog";

			default:
				break;
			}
		}

		// Antworten für all User
		if (userAndGroups.isUser(t)) {
			switch (sInMsg) {
			case "user":
				return "User OK";

			// Grafana
			case "grafana":
			case "heizung":
			case "h":
				return grafana.telegramMenuehHeizung(t);

			case "s":
			case "solar":
				return grafana.telegramMenueSolar(t);

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
			return wipf.getTime("dd.MM.yyyy HH:mm:ss");
		case "witz":
		case "fun":
		case "w":
		case "joke":
			return appOthers.getWitz();
		case "countmsg":
			return appMsg.countMsg().toString();
		case "getmyid":
		case "id":
		case "whoami":
		case "pwd":
		case "me":
		case "i":
			return "From: " + t.getFrom() + "\n\nChat: " + t.getChatID() + " " + t.getType() + "\n\nM_id: " + t.getMid();
		case "calc":
		case "math":
		case "m":
			return String.valueOf(wipf.doMathByString(t.getMessageFullWithoutFirstWord()));
		case "mills":
		case "ts":
			return String.valueOf(new Date().getTime());

		case "is":
		case "rechte":
		case "valide":
			return "Rechte: " + String.valueOf(userAndGroups.isUser(t));

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
