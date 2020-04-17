package org.wipf.jasmarty.logic.jasmarty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.ButtonAction;
import org.wipf.jasmarty.logic.base.MsqlLite;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ActionVerwaltung {

	private static final Logger LOGGER = Logger.getLogger("ActionVerwaltung");

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
	public void actionToDB(ButtonAction ba) throws SQLException {
		Statement stmt = MsqlLite.getDB();
		stmt.execute("INSERT OR REPLACE INTO pages (id, button, active, action) VALUES ('" + ba.getId() + "','"
				+ ba.getButton() + "','" + ba.isActive() + "','" + ba.getAction() + "')");
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByID(int nId) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pages WHERE id = '" + nId + "';");
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
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbByButton(int nButton) {
		try {
			ButtonAction ba = new ButtonAction();

			Statement stmt = MsqlLite.getDB();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pages WHERE button = '" + nButton + "' AND active = 1;");
			ba.setId(rs.getInt("id"));
			ba.setButton(rs.getInt("button"));
			ba.setActive(rs.getBoolean("active"));
			ba.setAction(rs.getString("action"));
			return ba;
		} catch (Exception e) {
			LOGGER.warn("BA not found by BNR: " + nButton);
			return new ButtonAction();
		}
	}

	/**
	 * @param nButton
	 */
	public void doAction(Integer nButton) {
		if (nButton != null) {
			System.out.println("Action: " + nButton);
			ButtonAction ba = getActionFromDbByButton(nButton);
			if (ba != null) {
				System.out.println("DO:" + ba.getAction());
			}
		}
	}

	public void setAction(String jnRoot) {
		try {
			actionToDB(new ButtonAction().setByJson(jnRoot));
		} catch (Exception e) {
			LOGGER.warn("Convert Page fehler");
		}
	}
}
