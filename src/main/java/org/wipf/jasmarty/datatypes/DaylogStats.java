package org.wipf.jasmarty.datatypes;

/**
 * @author wipf
 *
 */
public class DaylogStats {

	public Long anz;
	public Integer id;
	public Integer dateid;
	public String typid;
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
		this.id = id;
		this.dateid = dateid;
		this.typid = typid;
		this.text = text;
	}

}
