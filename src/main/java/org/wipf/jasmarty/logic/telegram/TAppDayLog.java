package org.wipf.jasmarty.logic.telegram;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.daylog.DaylogDay;
import org.wipf.jasmarty.datatypes.daylog.DaylogEvent;
import org.wipf.jasmarty.datatypes.daylog.DaylogType;
import org.wipf.jasmarty.datatypes.telegram.LastMessageCache;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.daylog.DaylogDayDB;
import org.wipf.jasmarty.logic.daylog.DaylogEventDB;
import org.wipf.jasmarty.logic.daylog.DaylogTypeDB;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class TAppDayLog {

	@Inject
	TLastMessageFromUser tLastMessageFromUser;
	@Inject
	DaylogTypeDB daylogTypeDB;
	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogEventDB daylogEventDB;
	@Inject
	Wipf wipf;

	public String telegramMenue(Telegram t) {
		try {
			return doDayLog(t);
		} catch (SQLException e) {
			e.printStackTrace();
			return "Fehler 672";
		}
	}

	public String doDayLog(Telegram t) throws SQLException {
		LastMessageCache lastMsgCache = tLastMessageFromUser.getLastMessage(t);

		if (lastMsgCache.getMsg().startsWith("dl start")) {
			// Schritt 1
			// Datum wählen
			Integer dateId = waehleDatum(t.getMessageFullWithoutFirstWord());
			if (dateId != null) {
				// DateId im Cache speichern
				lastMsgCache.setUsercache("dateid:" + dateId);
				tLastMessageFromUser.save(lastMsgCache); // Datum Id speichern

				// Kategorien Übersicht ausgeben
				return wipf.jsonArrayToStringAsList("Kategorienid wählen", daylogTypeDB.getAllAsJson());
			} else {
				return "Fehler mit den Datum";
			}
		} else if (lastMsgCache.getUsercache().startsWith("dateid:")) {
			// Schritt 2
			// Kategorie wählen
			Integer nKategorieId = t.getMessageIntPart(1);
			String sKatName = pruefeKategorie(nKategorieId);
			if (sKatName != null) {
				// katid vorne anfügen und Datum weiterhin speichern
				lastMsgCache.setUsercache("katid:" + nKategorieId + lastMsgCache.getUsercache());
				tLastMessageFromUser.save(lastMsgCache);

				return "Kategorie: " + sKatName + "\n\n Bitte Text eingeben";
			} else {
				return "Fehler mit der Kategorie";
			}
		} else if (lastMsgCache.getUsercache().startsWith("katid:")) {
			// Schitt 3
			// den Text holen und das event speichern
			String sEventtext = t.getMessageFullWithoutFirstWord();
			String sSaveResult = schreibeEvent(sEventtext, lastMsgCache);
			if (sSaveResult != null) {
				lastMsgCache.setUsercache("");
				tLastMessageFromUser.save(lastMsgCache);
				return sSaveResult;
			}
		}

		// ertaufruf
		switch (t.getMessageFullWithoutFirstWord()) {
		case "start":

		case "help":
		default:
			// @formatter:off
				return "Syntax:\n" + "\n"
						+ "1. dl start"
						+ "1. dl <DatumID oder h für Heute>"
						+ "2. dl <KATEGORIEID>"
						+ "3. dl <EventText>";
				// @formatter:on

		}
	}

	/**
	 * datums id zurückgeben
	 * 
	 * @param t
	 * @param lastMsgCache
	 * @return
	 */
	private Integer waehleDatum(String sDate) {
		if (sDate.equals("h")) {

			// Heute
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String sDateNow = df.format(new Date());

			try {
				// Tag heute erstellen
				// TODO USERID nicht auf 0 setzen
				DaylogDay dday = daylogDayDB.get(sDateNow, 0);
				if (dday.getId() != null) {
					// Tag existiert
					return dday.getId();
				} else {
					// Tag erstellen
					DaylogDay newDay = new DaylogDay();
					newDay.setUserId(0);
					newDay.setDate(sDateNow);
					daylogDayDB.save(newDay);

					// Nochmals nach der Id suchen, sollte jetzt da sein
					return daylogDayDB.get(sDateNow, 0).getId();
				}
			} catch (SQLException e) {
				System.out.println("Fehler Datum erstellen");
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println("TODO 7523489");
			// TODO: DATUM EINGEBEN UND DAY Ersteleln
			return null;
		}
	}

	/**
	 * 
	 * Kategorie id prüfen und Kat Name zurückgeben
	 * 
	 * 
	 * @param t
	 * @return
	 */
	private String pruefeKategorie(Integer nKatId) {
		try {
			List<DaylogType> dt = daylogTypeDB.get(nKatId);
			if (dt.size() == 1) {
				return dt.get(0).getType();
			} else {
				return null;
			}

		} catch (SQLException e) {
			System.out.println("Fehler pruefeKategorie");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param sEventtext
	 * @param lastMsgCache
	 * @return
	 * @throws SQLException
	 */
	private String schreibeEvent(String sEventtext, LastMessageCache lastMsgCache) {
		try {
			DaylogEvent dayEvent = new DaylogEvent();
			String sCache = lastMsgCache.getUsercache();
			// Der Cache schaut so aus: katid:42dateid:99
			Integer nDateId = Integer.valueOf(sCache.substring(sCache.indexOf(":"), sCache.indexOf("d")));
			// Auch als int validieren, wird aber als String gespeichert
			Integer nTyp = Integer.valueOf(sCache.substring(sCache.lastIndexOf(":"), sCache.length()));

			dayEvent.setDateId(nDateId);
			dayEvent.setTyp(nTyp.toString());
			dayEvent.setText(sEventtext);

			daylogEventDB.save(dayEvent);
			return "Speicheren von DateId: " + nDateId + " TypId: " + nTyp + " Text: " + sEventtext;

		} catch (SQLException e) {
			System.out.println("Fehler schreibeEvent");
			e.printStackTrace();
			return null;
		}

	}

}
