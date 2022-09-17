package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.daylog.DaylogDay;
import org.wipf.jasmarty.datatypes.daylog.DaylogEvent;
import org.wipf.jasmarty.datatypes.telegram.Telegram;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogHome {

	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogEventDB daylogEventDB;
	@Inject
	DaylogTypeDB daylogTypeDB;

	/**
	 * Hilfsklasse für etliche Ausgaben
	 *
	 */
	class TagesInfo {
		public String sInfo;
		public Integer nAnzahlEvents;
	}

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		daylogDayDB.initDB();
		daylogEventDB.initDB();
		daylogTypeDB.initDB();
	}

	/**
	 * Datum Heute
	 * 
	 * @return
	 */
	private TagesInfo getTagesInfo() {
		return getTagesinfoByDate(LocalDate.now().toString());
	}

	/**
	 * @return
	 */
	private TagesInfo getGesternInfo() {
		return getTagesinfoByDate(LocalDate.now().minusDays(1).toString());
	}

	/**
	 * Inhalt wenn morgen ein oder mehr Events vorhanden
	 * 
	 * Inhalt wenn gestern weniger als 4 einträge da sind
	 * 
	 * @return
	 */
	public String getDailyInfo() {
		StringBuilder sb = new StringBuilder();
		TagesInfo tiHeute = getTagesInfo();
		TagesInfo tiGestern = getGesternInfo();

		if (tiHeute.nAnzahlEvents > 0) {
			sb.append(tiHeute.sInfo);
			sb.append("\n\n");
		}

		if (tiGestern.nAnzahlEvents < 4) {
			sb.append(tiGestern.sInfo);
		}

		return sb.toString();
	}

	/**
	 * @param t
	 * @return
	 */
	public String getTagesinfoByTelegram(Telegram t) {
		if (t.getMessageFullWithoutFirstWord().matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
			return getTagesinfoByDate(t.getMessageFullWithoutFirstWord()).toString();
		} else if (t.getMessageFullWithoutFirstWord().toLowerCase().equals("g")) {
			// Gestern
			return getGesternInfo().sInfo;
		}
		// Kein Valides Datum mitgegeben -> gebe Heute zurück
		return getTagesInfo().sInfo;
	}

	/**
	 * @param t
	 * @return
	 */
	public String getAllUniqueEventTextByTyp(Telegram t) {
		try {
			List<DaylogEvent> events = daylogEventDB.getAllByTypId(t.getMessageIntPart(1).toString());

			List<String> elist = new LinkedList<String>();
			for (DaylogEvent ev : events) {
				if (!elist.contains(ev.getText())) {
					elist.add(ev.getText());
				}
			}

			StringBuilder sb = new StringBuilder();
			for (String s : elist) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(s);
			}
			return sb.toString();

		} catch (SQLException e) {

			return "Fehler 105: " + e;
		}
	}

	/**
	 * @param sDate
	 * @return
	 */
	private TagesInfo getTagesinfoByDate(String sDate) {
		TagesInfo ti = new TagesInfo();
		ti.nAnzahlEvents = 0;

		StringBuilder sb = new StringBuilder();
		try {
			DaylogDay dday = daylogDayDB.get(sDate, 0);
			if (dday.getId() == null) {
				ti.sInfo = "Für den Tag " + sDate + " gibt es keine Events";
				return ti;
			}
			List<DaylogEvent> dEvents = daylogEventDB.getByDateId(dday.getId());

			sb.append("Events für " + sDate + "\n-----------\n");
			if (dday.getTagestext() != null) {
				sb.append(dday.getTagestext() + "\n\n");
			}

			for (DaylogEvent dEvent : dEvents) {
				// Typ Text:
				sb.append(daylogTypeDB.get(dEvent.getTyp()).get(0).getType());
				sb.append(": \n");
				sb.append(dEvent.getText());
				sb.append("\n\n");
				ti.nAnzahlEvents++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			sb.append("Fehler getTagesinfo: " + e);
		}
		ti.sInfo = sb.toString();
		return ti;
	}

	/**
	 * mit Datumsauflösung
	 * 
	 * @param nDateId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllByTypIdAsJson(String sIds) throws SQLException {
		List<DaylogEvent> l = daylogEventDB.getAllByTypId(sIds);
		JSONArray ja = new JSONArray();
		for (DaylogEvent d : l) {
			JSONObject jo = d.toJson();
			// Das Datum auslösen und mitgeben
			jo.put("date", daylogDayDB.getById(d.getDateId()).getDate());
			ja.put(jo);
		}
		return ja;
	}

}