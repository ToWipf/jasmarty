package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	public String getTagesinfo() {
		// Datum Heute
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sDateNow = df.format(new Date());

		return getTagesinfoByDate(sDateNow);
	}

	/**
	 * @param t
	 * @return
	 */
	public String getTagesinfoByTelegram(Telegram t) {
		if (t.getMessageFullWithoutFirstWord().matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
			return getTagesinfoByDate(t.getMessageFullWithoutFirstWord());
		}

		return getTagesinfo();

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
			List<DaylogEvent> dEvents = daylogEventDB.get(dday.getId());

			sb.append("Events für " + sDate + "\n-----------\n");

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