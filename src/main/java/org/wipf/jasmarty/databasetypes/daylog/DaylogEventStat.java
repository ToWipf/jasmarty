package org.wipf.jasmarty.databasetypes.daylog;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DaylogEventStat extends DaylogEvent {

	private static final long serialVersionUID = 1L;

	public Integer anz;
	public Integer first_id;
	public Integer first_dateid;
	public Integer frist_typ;

//	public DaylogEventStat(Integer anz, Integer first_id, Integer first_dateid, Integer frist_typ, String text) {
//		this.anz = anz;
//		this.first_id = first_id;
//		this.first_dateid = first_dateid;
//		this.frist_typ = frist_typ;
//		this.text = text;
//	}

	// PanacheQuery<DaylogEventStat> query = DaylogEvent.find("select d.race,
	// AVG(d.weight) from Dog d group by d.race").project(DaylogEventStat.class);

	// PanacheQuery<DaylogEventStat> query = DaylogEvent.find("select COUNT(*) anz,
	// e from DaylogEvent e where typid IN ?1 GROUP by text ORDER by anz
	// DESC").project(DaylogEventStat.class);

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

	public static PanacheQuery<DaylogEventStat> getStatsByTypids(String typids) {
		return find("select COUNT(*) as anz, e from DaylogEvent e where typid IN ?1 GROUP by text", typids);

		// return find("select COUNT(*), e from DaylogEvent e where typid IN ?1 GROUP by
		// text", typids);

		// return find("select COUNT(*) anz, e from DaylogEvent e where typid IN ?1
		// GROUP by text ORDER by anz DESC", typids);
	}

}
