package org.wipf.jasmarty.datatypes.daylog;

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;

/**
 * @author wipf
 *
 */
public class DaylogStatsDiagram {

	public String date;
	public Integer id;
	public Integer dateid;
	public String typid;
	public String text;

	/**
	 * @param d
	 */
	public void set(DaylogEvent d) {
		this.id = d.id;
		this.dateid = d.dateid;
		this.typid = d.typid;
		this.text = d.text;
	}
}
