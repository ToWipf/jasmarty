package org.wipf.jasmarty.datatypes.daylog;

/**
 * @author Wipf
 *
 */
public class DaylogDay {

	private Integer id;
	private Integer nUserId;
	private String sDate;
	private String sTagestext;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return nUserId;
	}

	public void setUserId(Integer userId) {
		this.nUserId = userId;
	}

	public String getDate() {
		return sDate;
	}

	public void setDate(String date) {
		this.sDate = date;
	}

	public String getTagestext() {
		return sTagestext;
	}

	public void setTagestext(String tagestext) {
		this.sTagestext = tagestext;
	}

}
