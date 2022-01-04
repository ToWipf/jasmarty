package org.wipf.jasmarty.logic.telegram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.LastMessage;
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
		String sUpdate = "CREATE TABLE IF NOT EXISTS lastMsgFromUser (chatid INTEGER NOT NULL UNIQUE, msg TEXT, PRIMARY KEY(chatid));";
		sqlLite.getDbApp().prepareStatement(sUpdate).executeUpdate();
	}

	/**
	 * @param o
	 * @throws SQLException
	 */
	public void save(LastMessage o) throws SQLException {
		// insert
		String sUpdate = "INSERT OR REPLACE INTO lastMsgFromUser (chatid, msg) VALUES (?,?)";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate);
		statement.setInt(1, o.getChatId());
		statement.setString(2, o.getMsg());
		statement.executeUpdate();

	}

	/**
	 * @param jnRoot
	 * @return
	 * @throws SQLException
	 */
	public Boolean save(String jnRoot) throws SQLException {
		try {
			save(new LastMessage().setByJson(jnRoot));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param t
	 */
	public void saveByTelegram(Telegram t) {
		LastMessage lmsg = new LastMessage();
		lmsg.setChatId(t.getChatID());
		lmsg.setMsg(t.getMessage());
		try {
			save(lmsg);
		} catch (SQLException e) {
			System.out.println("Fehler beim speichern von LastMessage: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * @param nChatid
	 * @return
	 */
	public String getTheLastMessage(Telegram t) {
		try {
			List<LastMessage> lmsg = get(t.getChatID());
			return lmsg.get(0).getMsg();
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
	public List<LastMessage> get(Integer nChatid) throws SQLException {
		List<LastMessage> o = new LinkedList<>();

		String sQuery = "SELECT * FROM lastMsgFromUser WHERE chatid = ?;";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		statement.setInt(1, nChatid);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			LastMessage d = new LastMessage();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
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
		List<LastMessage> l = get(nChatid);
		JSONArray ja = new JSONArray();
		for (LastMessage d : l) {
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
	public List<LastMessage> getAll() throws SQLException {
		List<LastMessage> o = new LinkedList<>();

		String sQuery = "SELECT * FROM lastMsgFromUser";
		PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sQuery);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			LastMessage d = new LastMessage();
			d.setChatId(rs.getInt("chatid"));
			d.setMsg(rs.getString("msg"));
			o.add(d);
		}
		return o;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public JSONArray getAllAsJson() throws SQLException {
		List<LastMessage> l = getAll();
		JSONArray ja = new JSONArray();
		for (LastMessage d : l) {
			ja.put(d.toJson());
		}
		return ja;
	}

}
