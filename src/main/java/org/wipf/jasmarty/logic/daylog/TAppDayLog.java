package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.daylog.DaylogEvent;
import org.wipf.jasmarty.datatypes.daylog.DaylogType;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.datatypes.telegram.Usercache;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.telegram.TUsercache;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class TAppDayLog {

	@Inject
	TUsercache tUsercache;
	@Inject
	DaylogTypeDB daylogTypeDB;
	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogEventDB daylogEventDB;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Telegram Daylog");

	public String telegramMenue(Telegram t) {
		try {
			return doDayLog(t);
		} catch (SQLException e) {
			e.printStackTrace();
			return "Fehler 672";
		}
	}

	public String doDayLog(Telegram t) throws SQLException {
		Usercache userCache = tUsercache.getLastMessage(t.getChatID());

		if (userCache == null) {
			// Es ist noch kein Cache da -> eine normale nachricht schreiben und ihn zu
			// erstellen
			return "Fail 683";
		} else if (t.getMessageFullWithoutFirstWord().toLowerCase().equals("start")) {
			// Wenn start eingebene wurde, den Cache leeren
			userCache.setUsercache("");
			tUsercache.save(userCache);
			return "Bitte das Datum eingeben:" + "\n" + "h für heute" + "\n" + "Format: yyyy-MM-dd" + "\n"
					+ "Optional kann ein Tagestext angegeben werden";
		} else if (userCache.getUsercache().equals("")) {
			// Schritt 1
			// Datum wählen
			Integer dateId = waehleDatum(t.getMessageFullWithoutFirstWord());
			if (dateId != null) {
				// DateId im Cache speichern
				userCache.setUsercache("dateid:" + dateId);
				tUsercache.save(userCache); // Datum Id speichern

				// Kategorien Übersicht ausgeben
				return createTypeList();
			} else {
				return "Fehler mit den Datum";
			}
		} else if (userCache.getUsercache().startsWith("dateid:")) {
			// Schritt 2
			// Kategorie wählen
			Integer nKategorieId = t.getMessageIntPart(1);
			String sKatName = pruefeKategorie(nKategorieId);
			if (sKatName != null) {
				// katid vorne anfügen und Datum weiterhin speichern
				userCache.setUsercache("katid:" + nKategorieId + userCache.getUsercache());
				tUsercache.save(userCache);

				return "Kategorie: " + sKatName + "\n" + "Bitte Text eingeben";
			} else {
				return "Fehler mit der Kategorie";
			}
		} else if (userCache.getUsercache().startsWith("katid:")) {
			// Schitt 3
			// den Text holen und das event speichern
			String sEventtext = t.getMessageFullWithoutFirstWord();
			String sSaveResult = schreibeEvent(sEventtext, userCache);
			if (sSaveResult != null) {
				userCache.setUsercache("");
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
		try {
			sb.append("Kategorienid wählen:\n\n");

			for (DaylogType dt : daylogTypeDB.getAll()) {
				sb.append(dt.getId());
				sb.append(": ");
				sb.append(dt.getType());
				sb.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Fehler 643";
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
	private Integer waehleDatum(String sDateInfoString) {
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
			if (sDateInfoString.startsWith("h")) {
				// Heute
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String sDateNow = df.format(new Date());
				return daylogDayDB.getDateAndCrateIfDateStringNotExists(sDateNow, sDateTagestext).getId();

			} else if (sDateInfoString.startsWith("g")) {
				// Gestern
				LocalDate dateGestern = LocalDate.now().minusDays(1);
				return daylogDayDB.getDateAndCrateIfDateStringNotExists(dateGestern.toString(), sDateTagestext).getId();
			} else {
				// Exakt
				String sDateString = sDateInfoString.substring(0, 10); // Länge des Datums ist immer fest
				if (sDateString.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
					// Ein Datum
					return daylogDayDB.getDateAndCrateIfDateStringNotExists(sDateString, sDateTagestext).getId();

				}
			}
		} catch (Exception e) {
			LOGGER.info("Fehler Datum erstellen: " + e);
		}
		return null;
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
		if (nKatId != null) {
			try {
				List<DaylogType> dt = daylogTypeDB.get(nKatId);
				if (dt.size() == 1) {
					return dt.get(0).getType();
				} else {
					return null;
				}
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * @param sEventtext
	 * @param lastMsgCache
	 * @return
	 * @throws SQLException
	 */
	private String schreibeEvent(String sEventtext, Usercache lastMsgCache) {
		try {
			DaylogEvent dayEvent = new DaylogEvent();
			String sCache = lastMsgCache.getUsercache();
			// Der Cache schaut so aus: katid:42dateid:99
			Integer nTyp = Integer.valueOf(sCache.substring(sCache.indexOf(":") + 1, sCache.indexOf("dateid")));
			// Auch als int validieren, wird aber als String gespeichert
			Integer nDateId = Integer.valueOf(sCache.substring(sCache.lastIndexOf(":") + 1, sCache.length()));

			dayEvent.setDateId(nDateId);
			dayEvent.setTyp(nTyp.toString());
			dayEvent.setText(sEventtext);

			daylogEventDB.save(dayEvent);
			return "Speicheren von DateId: " + nDateId + " TypId: " + nTyp + " Text: " + sEventtext;

		} catch (SQLException e) {
			LOGGER.info("Fehler schreibeEvent " + e);
			return null;
		}

	}

}
