package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.LastMessageCache;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.SqlLite;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class TLastMessageFromUser {

	@Inject
	SqlLite sqlLite;

	/**
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		String sUpdate = "CREATE TABLE IF NOT EXISTS lastMsgFromUser (chatid INTEGER NOT NULL UNIQUE, msg TEXT, usercache TEXT, PRIMARY KEY(chatid));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * Usercache überschreiben
	 * 
	 * @param o
	 * @throws SQLException
	 */
	public void save(LastMessageCache o) throws SQLException {
		// insert
		String sUpdate = "INSERT OR REPLACE INTO lastMsgFromUser (chatid, msg, usercache) VALUES (?,?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, o.getChatId());
		statement.setString(2, o.getMsg());
		statement.setString(3, o.getUsercache());
		statement.executeUpdate();
	}

	/**
	 * Speichern ohne den Usercache zu ändern
	 * 
	 * @param o
	 * @throws SQLException
	 */
	public void saveOhneUsercache(LastMessageCache o) throws SQLException {
		// insert
		String sUpdate = "INSERT OR REPLACE INTO lastMsgFromUser (chatid, msg) VALUES (?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, o.getChatId());
		statement.setString(2, o.getMsg());
		statement.executeUpdate();

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
			save(new LastMessageCache().setByJson(jnRoot));
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
		LastMessageCache lmsg = new LastMessageCache();
		lmsg.setChatId(t.getChatID());
		lmsg.setMsg(t.getMessage());
		try {
			saveOhneUsercache(lmsg);
		} catch (SQLException e) {
			System.out.println("Fehler1 beim speichern von LastMessage: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * @param nChatid
	 * @return
	 */
	public LastMessageCache getLastMessage(Telegram t) {
		try {
			List<LastMessageCache> lmsg = get(t.getChatID());
			return lmsg.get(0);
		} catch (SQLException e) {
			// nichts vorhanden
			return null;
		}

	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<LastMessageCache> get(Integer nChatid) throws SQLException {
		List<LastMessageCache> o = new LinkedList<>();

		String sQuery = "SELECT * FROM lastMsgFromUser WHERE chatid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nChatid);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			LastMessageCache d = new LastMessageCache();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
			d.setUsercache(rs.getString("usercache"));
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
		List<LastMessageCache> l = get(nChatid);
		JSONArray ja = new JSONArray();
		for (LastMessageCache d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

	/**
	 * @param sDate
	 * @throws SQLException
	 */
	public void del(Integer nId) throws SQLException {
		String sUpdate = "DELETE FROM lastMsgFromUser WHERE id = ?";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, nId);
		statement.executeUpdate();
	}

	/**
	 * @param nUserId
	 * @return
	 * @throws SQLException
	 */
	public List<LastMessageCache> getAll() throws SQLException {
		List<LastMessageCache> o = new LinkedList<>();

		String sQuery = "SELECT * FROM lastMsgFromUser";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			LastMessageCache d = new LastMessageCache();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
			d.setUsercache(rs.getString("usercache"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<LastMessageCache> l = getAll();
		JSONArray ja = new JSONArray();
		for (LastMessageCache d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
