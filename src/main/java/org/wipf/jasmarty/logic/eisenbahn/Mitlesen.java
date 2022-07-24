package org.wipf.jasmarty.logic.eisenbahn;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;
import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Mitlesen {

	@Inject
	MitlesenConnect connect;
	@Inject
	WipfConfig wipfConfig;

	private static final String PORT = "arduino_Port";
	private static final String BAUDRATE = "arduino_BaudRate";
	private static final String LINELENGTH = "arduino_LineLength";

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen");
	private boolean bActive = false;
	private HashMap<String, Integer> itemMap = new HashMap<String, Integer>();
	private Integer nAnzahlLeerstring = 0;

	/**
	 * 
	 */
	public void doStartMitlesen() {
		if (!bActive) {
			LOGGER.info("starten");

			bActive = true;
			nAnzahlLeerstring = 0;
			ExecutorService service = Executors.newFixedThreadPool(1);
			service.submit(new Runnable() {

				@Override
				public void run() {
					while (bActive) {
						String sIn = connect.getLine();
						if (sIn != null) {

							addItem(sIn);

							if (sIn.isEmpty()) {
								nAnzahlLeerstring++;

								// Leere Antworten sind Fehler!
								if (nAnzahlLeerstring > 150) {
									LOGGER.warn("Zu viele Fehler/Leere Antworten");
									bActive = false;
								}
							}
						}
					}
				}
			});
		} else {
			LOGGER.info("bereits aktiv");
		}
	}

	/**
	 * 
	 */
	public void doStopMitlesen() {
		LOGGER.info("stop");
		bActive = false;
	}

	/**
	 * @param s
	 */
	private void addItem(String s) {
		Integer nOldAnzahl = itemMap.get(s);
		if (nOldAnzahl == null) {
			nOldAnzahl = 0;
		}
		itemMap.put(s, nOldAnzahl + 1);
	}

	/**
	 * @return
	 */
	public JSONArray getList() {
		if (!bActive) {
			return null;
		}

		JSONArray ja = new JSONArray();
		try {
			itemMap.forEach((key, val) -> {
				JSONObject o = new JSONObject();
				o.put("key", key);
				o.put("val", val);
				ja.put(o);
			});
		} catch (Exception e) {
			LOGGER.warn("getAllAsJson " + e);
			e.printStackTrace();
		}
		return ja;
	}

	/**
	 * @param s
	 */
	public void addDebugItem(String s) {
		addItem(s);
	}

	/**
	 * @return
	 */
	public Boolean connect() {
		setConfig();
		return connect.startSerialPort();
	}

	/**
	 * 
	 */
	public void setConfig() {
		try {
			ArduinoConfig ac = new ArduinoConfig();
			ac.setPort(wipfConfig.getConfParamString(PORT));
			ac.setBaudRate(wipfConfig.getConfParamInteger(BAUDRATE));
			ac.setLineLength(wipfConfig.getConfParamInteger(LINELENGTH));
			if (ac.isValid()) {
				LOGGER.info("Config ok");
				connect.setConfig(ac);
				// mitlesenConnect.startSerialPort();
				// mitlesen.doStartMitlesen();
			} else {
				LOGGER.error("Problem mit der Config");
				wipfConfig.setConfParam(PORT, "COM3");
				wipfConfig.setConfParam(BAUDRATE, 57600);
				wipfConfig.setConfParam(LINELENGTH, 50);
			}

			ac.setPort(wipfConfig.getConfParamString(PORT));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
