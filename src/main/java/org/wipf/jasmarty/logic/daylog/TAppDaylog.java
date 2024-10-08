package org.wipf.jasmarty.logic.daylog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;
import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.databasetypes.daylog.DaylogType;
import org.wipf.jasmarty.databasetypes.telegram.Usercache;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.telegram.TUsercache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppDaylog {

	@Inject
	TUsercache tUsercache;
	@Inject
	DaylogTypeService daylogTypeDB;
	@Inject
	DaylogDayService daylogDayDB;
	@Inject
	DaylogEventService daylogEventDB;
	@Inject
	Wipf wipf;

	/**
	 * dl
	 * 
	 * @param t
	 * @return
	 */
	public String telegramMenue(Telegram t) {
		return doDayLog(t);
	}

	/**
	 * @param t
	 * @return
	 */
	public String doDayLog(Telegram t) {
		Usercache userCache = tUsercache.getLastMessage(t.getChatID());

		if (userCache == null) {
			// Es ist noch kein Cache da -> eine normale nachricht schreiben und ihn zu
			// erstellen
			return "Fail 683";
		} else if (t.getMessageFullWithoutFirstWord().toLowerCase().equals("start")) {
			// Wenn start eingebene wurde, den Cache leeren
			userCache.usercache = ("");
			tUsercache.save(userCache);
			return "Bitte das Datum eingeben:" + "\n" + "h für heute" + "\n" + "Format: yyyy-MM-dd" + "\n" + "Optional kann ein Tagestext angegeben werden";
		} else if (userCache.usercache.equals("")) {
			// Schritt 1
			// Datum wählen
			DaylogDay ddate = waehleDatum(t.getMessageFullWithoutFirstWord());
			if (ddate != null) {
				// DateId im Cache speichern
				userCache.usercache = "dateid:" + ddate.id;
				tUsercache.save(userCache); // Datum Id speichern

				// Kategorien Übersicht ausgeben
				return "Datum: " + ddate.date + "\n" + ddate.tagestext + "\n" + createTypeList();
			} else {
				return "Fehler mit den Datum -> Anleitung: dl Start";
			}
		} else if (userCache.usercache.startsWith("dateid:")) {
			// Schritt 2
			// Kategorie wählen
			Integer nKategorieId = t.getMessageIntPart(1);
			String sKatName = pruefeKategorie(nKategorieId);
			if (sKatName != null) {
				// katid vorne anfügen und Datum weiterhin speichern
				userCache.usercache = "katid:" + nKategorieId + userCache.usercache;
				tUsercache.save(userCache);

				return "Kategorie: " + sKatName + "\n" + "Bitte Text eingeben";
			} else {
				return "Fehler mit der Kategorie";
			}
		} else if (userCache.usercache.startsWith("katid:")) {
			// Schitt 3
			// den Text holen und das event speichern
			String sEventtext = t.getMessageFullWithoutFirstWord();
			String sSaveResult = schreibeEvent(sEventtext, userCache);
			if (sSaveResult != null) {
				userCache.usercache = "";
				tUsercache.save(userCache);
				return sSaveResult;
			}
		}

		// Ansonsten die Hilfe zeigen
		// @formatter:off
		return "Syntax:\n" + "\n"
				+ "1. dl start" + "\n"
				+ "2. dl <DatumID oder h für Heute> optionaler Text" + "\n"
				+ "3. dl <KATEGORIEID>" + "\n"
				+ "4. dl <EventText>";
		// @formatter:on

	}

	/**
	 * @return
	 */
	private String createTypeList() {
		StringBuilder sb = new StringBuilder();
		sb.append("Kategorienid wählen:\n\n");

		for (DaylogType dt : daylogTypeDB.getAll()) {
			sb.append(dt.id);
			sb.append(": ");
			sb.append(dt.type);
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * datums id zurückgeben
	 * 
	 * @param t
	 * @param lastMsgCache
	 * @return
	 */
	private DaylogDay waehleDatum(String sDateInfoString) {
		// Ersten part bekommen
		// Mögliche syntax: h TEXT
		// Mögliche syntax: 2022-02-02 TEXT

		// Prüfen ob es einen TagesText gibt
		String sDateTagestext = "";
		Integer nDateTextStart = sDateInfoString.indexOf(" ");
		if (nDateTextStart > 0) {
			sDateTagestext = sDateInfoString.substring(nDateTextStart).trim();
		}

		try {
			if (sDateInfoString.startsWith("h") || sDateInfoString.startsWith("H")) {
				// Heute
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String sDateNow = df.format(new Date());
				return getDateAndCrateIfDateStringNotExists(sDateNow, sDateTagestext);

			} else if (sDateInfoString.startsWith("g") || sDateInfoString.startsWith("G")) {
				// Gestern
				LocalDate dateGestern = LocalDate.now().minusDays(1);
				return getDateAndCrateIfDateStringNotExists(dateGestern.toString(), sDateTagestext);
			} else {
				// Exakt
				String sDateString = sDateInfoString.substring(0, 10); // Länge des Datums ist immer fest
				if (sDateString.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
					// Ein Datum
					return getDateAndCrateIfDateStringNotExists(sDateString, sDateTagestext);

				}
			}
		} catch (Exception e) {
			// LOGGER.info("Fehler Datum erstellen: " + e);
		}
		return null;
	}

	/**
	 * Kategorie id prüfen und Kat Name zurückgeben
	 * 
	 * @param t
	 * @return
	 */
	private String pruefeKategorie(Integer nKatId) {
		if (nKatId != null) {
			return daylogTypeDB.get(nKatId).type;
		}
		return null;
	}

	/**
	 * @param sEventtext
	 * @param lastMsgCache
	 * @return
	 */
	private String schreibeEvent(String sEventtext, Usercache lastMsgCache) {
		DaylogEvent dayEvent = new DaylogEvent();
		String sCache = lastMsgCache.usercache;
		// Der Cache schaut so aus: katid:42dateid:99
		Integer nTyp = Integer.valueOf(sCache.substring(sCache.indexOf(":") + 1, sCache.indexOf("dateid")));
		// Auch als int validieren, wird aber als String gespeichert
		Integer nDateId = Integer.valueOf(sCache.substring(sCache.lastIndexOf(":") + 1, sCache.length()));

		dayEvent.dateid = nDateId;
		dayEvent.typid = nTyp.toString();
		dayEvent.text = sEventtext;

		daylogEventDB.save(dayEvent);

		// Nochmals den Tag laden um das Datum zu zeigen
		String sDatum = "Fehler 108";
		try {

			sDatum = daylogDayDB.getById(nDateId).date;
		} catch (Exception e) {
			System.err.println("Fehler 109");
			e.printStackTrace();
		}

		return "Speicheren von " + sDatum + " mit der TypId: " + nTyp + " und den Text: " + sEventtext;
	}

	/**
	 * @param sDateNow
	 * @param sDateTagestext
	 * @return
	 */
	public DaylogDay getDateAndCrateIfDateStringNotExists(String sDate, String sDateTagestext) {
		DaylogDay d = daylogDayDB.getByDateString(sDate);
		if (d == null) {
			d = new DaylogDay();
			d.date = sDate;
			d.tagestext = sDateTagestext;
		} else {
			// schon existent
			if (!d.tagestext.isBlank()) {
				d.tagestext = sDateTagestext;
			}
		}
		d.saveOrUpdate();
		return d;
	}

}
