package org.wipf.jasmarty.logic.daylog;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;
import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.datatypes.daylog.DaylogStatsDiagram;
import org.wipf.jasmarty.datatypes.telegram.Telegram;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**
 * @author Wipf
 *
 */
@RequestScoped
public class DaylogHome {

	@Inject
	DaylogDayService daylogDayDB;
	@Inject
	DaylogEventService daylogEventDB;
	@Inject
	DaylogTypeService daylogTypeDB;

	/**
	 * Hilfsklasse für etliche Ausgaben
	 *
	 */
	class TagesInfo {
		public String sInfo;
		public Integer nAnzahlEvents;
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
		List<DaylogEvent> events = daylogEventDB.getAllByTypId(t.getMessageIntPart(1).toString());
		List<String> elist = new LinkedList<String>();
		for (DaylogEvent ev : events) {
			if (!elist.contains(ev.text)) {
				elist.add(ev.text);
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
			DaylogDay dday = daylogDayDB.get(sDate);
			if (dday == null) {
				ti.sInfo = "Für den Tag " + sDate + " gibt es keine Events";
				return ti;
			}
			List<DaylogEvent> dEvents = daylogEventDB.getByDateId(dday.id);

			sb.append("Events für " + sDate + "\n-----------\n");
			if (dday.tagestext != null) {
				sb.append(dday.tagestext + "\n\n");
			}

			for (DaylogEvent dEvent : dEvents) {
				// Typ Text:
				sb.append(daylogTypeDB.get(Integer.valueOf(dEvent.typid)).type);
				sb.append(": \n");
				sb.append(dEvent.text);
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
	 */
	public List<DaylogStatsDiagram> getAllByTypIdAsJson(String sIds) {
		List<DaylogStatsDiagram> ldsd = new LinkedList<>();
		daylogEventDB.getAllByTypIds(sIds).forEach(d -> {
			DaylogStatsDiagram dsd = new DaylogStatsDiagram();
			dsd.set(d);
			dsd.date = daylogDayDB.getById(d.dateid).date;
			ldsd.add(dsd);
		});

		return ldsd;
	}

}