package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.ButtonAction;
import org.wipf.jasmarty.logic.base.MsqlLite;
import org.wipf.jasmarty.logic.jasmarty.extensions.Tastatur;
import org.wipf.jasmarty.logic.jasmarty.extensions.Winamp;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ActionVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("ActionVerwaltung");
	private Integer currentPressed;

	@Inject
	LcdConnect lcdConnect;
	@Inject
	PageVerwaltung pageVerwaltung;
	@Inject
	Tastatur tastatur;
	@Inject
	Winamp winamp;

	/**
	 * @throws SQLException
	 */
	public void initDB() {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS actions (id INTEGER UNIQUE, button INTEGER , active INTEGER , action TEXT);");
		} catch (Exception e) {
			LOGGER.error("init DB");
		}
	}

	/**
	 * @param ba
	 * @throws SQLException
	 */
	public void saveToDB(ButtonAction ba) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO actions (id, button, active, action) VALUES ('" + ba.getId() + "','"
				+ ba.getButton() + "','" + ba.isActive() + "','" + ba.getAction() + "')");
	}

	/**
	 * @param nId
	 */
	public void delete(Integer nId) {
		try {
			Statement stmt = MsqlLite.getDB();
			stmt.execute("DELETE FROM actions WHERE id LIKE '" + nId + "';");
		} catch (Exception e) {
			LOGGER.warn("del" + e);
		}
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByID(int nId) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM actions WHERE id = '" + nId + "';");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			LOGGER.warn("BA not found: " + nId);
			return new ButtonAction();
		}
	}

	/**
	 * @return
	 */
	public JSONArray getAllFromDBAsJson() {
		JSONArray ja = new JSONArray();
		try {
			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("select * from actions;");
			while (rs.next()) {
				JSONObject entry = new JSONObject();
				entry.put("id", rs.getInt("id"));
				entry.put("button", rs.getInt("button"));

				if (rs.getString("active").equals("true")) {
					entry.put("active", true);
				} else {
					entry.put("active", false);
				}

				entry.put("action", rs.getString("action"));
				ja.put(entry);
			}
			rs.close();
		} catch (Exception e) {
			LOGGER.warn("getAllFromDBAsJson: " + e);
		}
		return ja;
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByButton(int nButton) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM actions WHERE button = '" + nButton + "' AND active = 'true';");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbById(int nId) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM actions WHERE id = '" + nId + "';");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param jnRoot
	 */
	public void setAction(String jnRoot) {
		try {
			saveToDB(new ButtonAction().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}

	/**
	 * @return
	 */
	public Integer getCurrentPressed() {
		return currentPressed;
	}

	/**
	 * @param nButton
	 */
	public void testActionById(Integer nButtonId) {
		try {
			doActionById(nButtonId);
		} catch (Exception e) {
			LOGGER.warn("testAction fehler: " + e);
		}

	}

	/**
	 * @param nButton
	 * @throws Exception
	 */
	public void doActionByButtonNr(Integer nButton) throws Exception {
		this.currentPressed = nButton;
		if (nButton != null) {
			ButtonAction ba = getActionFromDbByButton(nButton);
			doActionByButton(ba);
		}
	}

	/**
	 * @param nButtonId
	 * @throws Exception
	 */
	public void doActionById(Integer nButtonId) throws Exception {
		if (nButtonId != null) {
			ButtonAction ba = getActionFromDbById(nButtonId);
			doActionByButton(ba);
		}
	}

	/**
	 * @param ba
	 * @throws Exception
	 */
	private void doActionByButton(ButtonAction ba) throws Exception {
		if (ba != null) {

			Integer nTrennlineFirst = ba.getAction().indexOf('|');
			Integer nTrennlineLast = ba.getAction().lastIndexOf('|');

			String sParameter1 = ba.getAction().substring(0, nTrennlineFirst);
			String sParameter2 = null;
			String sParameter3 = null;

			if (nTrennlineFirst != nTrennlineLast) {
				// es gibt einen 3. Parameter
				sParameter2 = ba.getAction().substring(nTrennlineFirst + 1, nTrennlineLast);
				sParameter3 = ba.getAction().substring(nTrennlineLast + 1);
			} else {
				// Es gibt nur 2 Parameter
				sParameter2 = ba.getAction().substring(nTrennlineFirst + 1);
			}

			switch (sParameter1) {
			case "led":
				switch (sParameter2) {
				case "on":
					lcdConnect.ledOn();
					return;
				case "off":
					lcdConnect.ledOff();
					return;
				case "toggle":
					lcdConnect.ledToggle();
					return;
				}
				return;
			case "page":
				switch (sParameter2) {
				case "next":
					pageVerwaltung.nextPage();
					return;
				case "last":
					pageVerwaltung.lastPage();
					return;
				case "select":
					pageVerwaltung.selectPage(sParameter3);
					return;
				}
				return;
			case "write":
				tastatur.write(sParameter2, sParameter3);
				return;
			case "winamp":
				winamp.control(sParameter2, sParameter3);
				return;
			case "volume":
				switch (sParameter2) {
				case "up":
					lcdConnect.commandVolUp();
					return;
				case "down":
					lcdConnect.commandVolDown();
					return;
				case "mute":
					lcdConnect.commandVolMute();
					return;
				}
				return;
			case "exec":
			case "system":

			default:
				LOGGER.warn("Aktion nicht verfügbar: " + sParameter1);
				return;
			}
		} else {
			LOGGER.warn("Eingang nicht definert: " + currentPressed);
		}
	}

}
