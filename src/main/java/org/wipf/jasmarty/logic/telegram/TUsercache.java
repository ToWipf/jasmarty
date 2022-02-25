package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.datatypes.telegram.Usercache;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class TUsercache {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS teleUsercache (chatid INTEGER NOT NULL UNIQUE, msg TEXT, usercache TEXT, counter INTEGER, PRIMARY KEY(chatid));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * Usercache überschreiben
	 * 
	 * @param o
	 * @throws SQLException
	 */
	public void save(Usercache o) throws SQLException {
		// Usercache uLast = getLastMessage(o.getChatId());
		// if (uLast != null) {
		//
		// // Beim speichern den Counter hochzählen
		// if (uLast.getCounter() != null) {
		// o.setCounter(uLast.getCounter() + 1);
		// }
		// } else {
		// // neuer User
		// o.setCounter(1);
		// }
		// insert
		String sUpdate = "INSERT OR REPLACE INTO teleUsercache (chatid, msg, usercache, counter) VALUES (?,?,?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, o.getChatId());
		statement.setString(2, o.getMsg());
		statement.setString(3, o.getUsercache());
		statement.setInt(4, o.getCounter());
		statement.executeUpdate();
	}

	/**
	 * Speichern ohne den Usercache zu ändern
	 * 
	 * @param o
	 * @throws SQLException
	 */
	public void saveOhneUsercache(Usercache o) throws SQLException {
		// Usercache vom letzten mal uebertragen
		Usercache last = getLastMessage(o.getChatId());
		if (last != null) {
			// Daten nur übertagen
			o.setUsercache(last.getUsercache());
			o.setCounter(last.getCounter());
		} else {
			// Neuer User
			o.setCounter(1);
		}

		save(o);
	}

	/**
	 * Usercache überschreiben
	 * 
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		try {
			save(new Usercache().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Speichern ohne den Usercache anzufassen
	 * 
	 * @param t
	 */
	public void saveByTelegramOhneUsercache(Telegram t) {
		Usercache lmsg = new Usercache();
		lmsg.setChatId(t.getChatID());
		lmsg.setMsg(t.getMessage());
		try {
			saveOhneUsercache(lmsg);
		} catch (SQLException e) {
			System.out.println("Fehler1 beim speichern von LastMessage: " + e);
		}
	}

	/**
	 * @param nChatid
	 * @return
	 */
	public Usercache getLastMessage(Integer nChatId) {
		try {
			List<Usercache> lmsg = get(nChatId);
			Usercache u = lmsg.get(0);
			// Bei jeden Laden den Counter hochzählen um beim speichern aktuell zu sein
			if (u.getCounter() != null) {
				u.setCounter(u.getCounter() + 1);
			} else {
				// neuer User
				u.setCounter(1);
			}

			return u;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<Usercache> get(Integer nChatid) throws SQLException {
		List<Usercache> o = new LinkedList<>();

		String sQuery = "SELECT * FROM teleUsercache WHERE chatid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nChatid);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			Usercache d = new Usercache();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
			d.setUsercache(rs.getString("usercache"));
			d.setCounter(rs.getInt("counter"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @param nDateId
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAsJson(Integer nChatid) throws SQLException {
		List<Usercache> l = get(nChatid);
		JSONArray ja = new JSONArray();
		for (Usercache d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM teleUsercache WHERE chatid = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<Usercache> getAll() throws SQLException {
		List<Usercache> o = new LinkedList<>();

		String sQuery = "SELECT * FROM teleUsercache";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			Usercache d = new Usercache();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
			d.setUsercache(rs.getString("usercache"));
			d.setCounter(rs.getInt("counter"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<Usercache> l = getAll();
		JSONArray ja = new JSONArray();
		for (Usercache d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public String getAllAsText() {
		StringBuilder sb = new StringBuilder();
		Integer n = 0;
		try {
			for (Usercache uc : getAll()) {
				if (sb.length() > 1) {
					sb.append("\n\n");
				}
				sb.append(uc.toString());
				n++;
			}
		} catch (SQLException e) {
			sb.append("Fehler 012 " + e);
		}
		return sb.toString() + " Anzahl: " + n;
	}

}
