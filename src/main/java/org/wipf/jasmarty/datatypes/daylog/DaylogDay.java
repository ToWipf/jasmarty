package org.wipf.jasmarty.datatypes.daylog;

import org.json.JSONObject;
import org.wipf.jasmarty.WipfException;

/**
 * @author Wipf
 *
 */
public class DaylogDay {

	private Integer nId;
	private Integer nUserId;
	private String sDate;
	private String sTagestext;

	/**
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("userid", this.nUserId);
		jo.put("date", this.sDate);
		jo.put("tagestext", this.sTagestext);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 * @throws WipfException
	 */
	public DaylogDay setByJson(String sJson) throws WipfException {
		JSONObject jo = new JSONObject(sJson);
		if (jo.has("id")) {
			this.nId = jo.getInt("id");
		}
		if (jo.has("userid")) {
			this.nUserId = jo.getInt("userid");
		} else {
			this.nUserId = 0;
		}
		this.sDate = jo.getString("date");
		this.sTagestext = jo.getString("tagestext").trim();

		if (sDate.isBlank()) {
			throw new WipfException("Datum darf nicht leer sein");
		}

		return this;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return nId;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.nId = id;
	}

	/**
	 * @return
	 */
	public Integer getUserId() {
		return nUserId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(Integer userId) {
		this.nUserId = userId;
	}

	/**
	 * @return
	 */
	public String getDate() {
		return sDate;
	}

	/**
	 * @param date
	 */
	public void setDate(String date) {
		this.sDate = date;
	}

	/**
	 * @return
	 */
	public String getTagestext() {
		return sTagestext;
	}

	/**
	 * @param tagestext
	 */
	public void setTagestext(String tagestext) {
		this.sTagestext = tagestext;
	}

}
