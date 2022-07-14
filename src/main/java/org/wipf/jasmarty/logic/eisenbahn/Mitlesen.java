package org.wipf.jasmarty.logic.eisenbahn;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Mitlesen {

	@Inject
	MitlesenConnect connect;
	@Inject
	MitlesenHome home;

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen");
	private boolean bActive = false;
	private HashMap<String, Integer> itemMap = new HashMap<String, Integer>();

	/**
	 * 
	 */
	public void doStartMitlesen() {
		if (!bActive) {
			LOGGER.info("starten");
			home.start();
			connect.startSerialPort();

			bActive = true;
			ExecutorService service = Executors.newFixedThreadPool(1);
			service.submit(new Runnable() {

				@Override
				public void run() {
					while (bActive) {
						String sIn = connect.getLine();
						if (sIn != null) {
							addItem(sIn);
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
		connect.closeSerialLcdPort();

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

}
