package org.wipf.jasmarty.databasetypes.daylog;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DaylogEventStat {

	public final Integer anz;
	public final Integer first_id;
	public final Integer first_dateid;
	public final Integer frist_typ;
	public final String text;
	public Integer id;
	public Integer dateid;
	public String typid;

	public DaylogEventStat(Integer anz, Integer first_id, Integer first_dateid, Integer frist_typ, String text) {
		this.anz = anz;
		this.first_id = first_id;
		this.first_dateid = first_dateid;
		this.frist_typ = frist_typ;
		this.text = text;
	}

	// PanacheQuery<DaylogEventStat> query = DaylogEvent.find("select d.race,
	// AVG(d.weight) from Dog d group by d.race").project(DaylogEventStat.class);
	PanacheQuery<DaylogEventStat> query = DaylogEvent.find("select COUNT(*) anz, e from DaylogEvent e where typ IN ?1 GROUP by text ORDER by anz DESC").project(DaylogEventStat.class);

	// "SELECT COUNT(*) anz, * from daylogTextEvent where typ IN (" + sTypIds +
	// ")GROUP by text ORDER by anz DESC";
//
//	class xxxtmp {
//		Integer anz;
//		Integer first_id;
//		Integer first_dateid;
//		Integer frist_typ;
//		String text;
//	}

//	public static PanacheQuery<xxxtmp> findByTypeIds(String sTypes) {
//		//return find("select COUNT(*) anz, e from DaylogEvent e where typ IN ?1 GROUP by text ORDER by anz DESC", sTypes);
//	}

}
