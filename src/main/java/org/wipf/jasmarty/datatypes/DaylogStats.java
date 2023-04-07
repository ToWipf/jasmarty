package org.wipf.jasmarty.datatypes;

/**
 * @author wipf
 *
 */
public class DaylogStats {

	public Long anz;
	public Integer first_id;
	public Integer first_dateid;
	public String frist_typid;
	public String text;

	/**
	 * @param anz
	 * @param id
	 * @param dateid
	 * @param typid
	 * @param text
	 */
	public DaylogStats(Long anz, Integer id, Integer dateid, String typid, String text) {
		this.anz = anz;
		this.first_id = id;
		this.first_dateid = dateid;
		this.frist_typid = typid;
		this.text = text;
	}

}
