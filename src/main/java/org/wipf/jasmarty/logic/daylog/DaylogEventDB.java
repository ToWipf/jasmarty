package org.wipf.jasmarty.logic.daylog;

import java.util.LinkedHashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class DaylogEventDB {

	/**
	 * @param o @
	 */
	@Transactional
	public void save(DaylogEvent o) {
		o.saveOrUpdate();
	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return @
	 */
	public List<DaylogEvent> getByDateId(Integer nId) {
		return DaylogEvent.findByDateId(nId).list();
	}

	/**
	 * @param nTypId
	 * @return @
	 */
	public List<DaylogEvent> getAllByTypId(String sTypIds) {
		return DaylogEvent.findByLikeType(sTypIds).list();
	}

	/**
	 * @param sDate @
	 */
	@Transactional
	public void del(Integer nId) {
		DaylogEvent.findById(nId).delete();
	}

	/**
	 * @param nUserId
	 * @return @
	 */
	public List<DaylogEvent> getAll() {
		return DaylogEvent.findAll().list();
	}

	/**
	 * @param sTypIds = "1,2,3,9";
	 * @return @
	 */
//	public JSONArray getStats(String sTypIds) {
//		JSONArray ar = new JSONArray();
//		String sQuery = "SELECT COUNT(*) anz, * from daylogTextEvent where typ IN (" + sTypIds + ") GROUP by text ORDER by anz DESC";
//		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
//		// statement.setString(1, sTypIds);
//		ResultSet rs = statement.executeQuery();
//
//		while (rs.next()) {
//			JSONObject oo = new JSONObject();
//			oo.put("anz", rs.getInt("anz"));
//			oo.put("first_id", rs.getInt("id"));
//			oo.put("first_dateid", rs.getInt("dateid"));
//			oo.put("frist_typ", rs.getInt("typ"));
//			oo.put("text", rs.getString("text"));
//			ar.put(oo);
//		}
//		return ar;
//	}

	/**
	 * @param sSearch
	 * @param sType
	 * @return @
	 */
	public LinkedHashSet<String> getTextBySearchAndType(String sSearch, String sType) {
		LinkedHashSet<String> o = new LinkedHashSet<>();

		System.out.println("XX");
		for (DaylogEvent d : DaylogEvent.findByINTypeANDText(sType, sSearch).list()) {
			o.add(d.text);
			System.out.println(d.toString());
		}

		return o;
	}

}
