package org.wipf.jasmarty.logic.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MsqlLite {

	private static final Logger LOGGER = Logger.getLogger("MsqlLite");
	private static final MsqlLite dbcontroller = new MsqlLite();
	private static Connection connection;

	/**
	 * 
	 */
	public static void startDB() {
		MsqlLite dbc = MsqlLite.getInstance();
		dbc.initDBConnection();
	}

	/**
	 * @return
	 */
	public static Statement getDB() {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			LOGGER.warn("getDB " + e);
			return null;
		}
	}

	/**
	 * 
	 */
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			LOGGER.warn("Fehler beim Laden des JDBC-Treibers " + e);
		}
	}

	/**
	 * @return
	 */
	private static MsqlLite getInstance() {
		return dbcontroller;
	}

	/**
	 * 
	 */
	private void initDBConnection() {
		try {
			if (connection != null) {
				LOGGER.warn("initDBConnection Fail C ");
				return;
			}
			LOGGER.info("Verbinde zu Datenbank '" + QMain.DB_PATH + "'");
			connection = DriverManager.getConnection("jdbc:sqlite:" + QMain.DB_PATH);
			if (!connection.isClosed()) {
				LOGGER.info("Datenbank Ok");
			}
		} catch (SQLException e) {
			LOGGER.warn("initDBConnection Fail A " + e);
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if (!connection.isClosed() && connection != null) {
						connection.close();
						if (connection.isClosed()) {
							LOGGER.info("Connection to Database closed");
							QMain.stopApp();
						}
					}
				} catch (SQLException e) {
					LOGGER.warn("initDBConnection Fail B " + e);
				}
			}
		});
	}
}
