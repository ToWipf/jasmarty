package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		daylogDayDB.initDB();
		daylogEventDB.initDB();
		daylogTypeDB.initDB();
	}

	/**
	 * @return
	 */
	public String getTagesInfo() {
		// Datum Heute
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sDateNow = df.format(new Date());

		return getTagesinfoByDate(sDateNow);
	}

	/**
	 * @return
	 */
	public String getGesternInfo() {
		LocalDate dateGestern = LocalDate.now().minusDays(1);
		return getTagesinfoByDate(dateGestern.toString());
	}

	/**
	 * @param t
	 * @return
	 */
	public String getTagesinfoByTelegram(Telegram t) {
		if (t.getMessageFullWithoutFirstWord().matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
			return getTagesinfoByDate(t.getMessageFullWithoutFirstWord());
		} else if (t.getMessageFullWithoutFirstWord().toLowerCase().equals("g")) {
			// Gestern
			return getGesternInfo();
		}
		// Kein Valides Datum mitgegeben -> gebe Heute zurück
		return getTagesInfo();
	}

	/**
	 * @param t
	 * @return
	 */
	public String getAllUniqueEventTextByTyp(Telegram t) {
		try {
			List<DaylogEvent> events = daylogEventDB.getByTypId(t.getMessageIntPart(1));

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
	private String getTagesinfoByDate(String sDate) {
		StringBuilder sb = new StringBuilder();
		try {
			DaylogDay dday = daylogDayDB.get(sDate, 0);
			if (dday.getId() == null) {
				return "Für den Tag " + sDate + " gibt es keine Events";
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			sb.append("Fehler getTagesinfo: " + e);
		}
		return sb.toString();
	}

}