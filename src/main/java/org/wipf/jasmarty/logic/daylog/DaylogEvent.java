package org.wipf.jasmarty.logic.daylog;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogEvent {

	@Inject
	DaylogDayDB daylogDayDB;
	@Inject
	DaylogTextEventDB daylogTextEventDB;
	@Inject
	DaylogBoolEventDB daylogBoolEventDB;
	@Inject
	DaylogNumberEventDB daylogNumberEventDB;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		daylogDayDB.initDB();
		daylogTextEventDB.initDB();
		daylogBoolEventDB.initDB();
		daylogNumberEventDB.initDB();
	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		JSONObject jo = new JSONObject(jnRoot);
		if (jo.has("text")) {
			return daylogTextEventDB.save(jnRoot);
		} else if (jo.has("bool")) {
			return daylogBoolEventDB.save(jnRoot);
		} else if (jo.has("number")) {
			return daylogNumberEventDB.save(jnRoot);
		} else {
			return false;
		}
	}

	/**
	 * @param nDateId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAsJson(Integer nDateId) throws SQLException {
		JSONArray ja = new JSONArray();
		ja.putAll(daylogTextEventDB.getAsJson(nDateId));
		ja.putAll(daylogBoolEventDB.getAsJson(nDateId));
		ja.putAll(daylogNumberEventDB.getAsJson(nDateId));
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		// TODO
	}

}