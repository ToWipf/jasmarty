package org.wipf.jasmarty.logic.lcd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.databasetypes.lcd.LcdPageDescription;

/**
 * Seiten Speichern und laden
 * 
 * Jede Page hat einen Festen Pixelspeicher und eine Beschreibung (Bauplan) des
 * Inhaltes
 * 
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864PageVerwaltung {

	@Inject
	Lcd12864PageConverter converter;

	/**
	 * @param page
	 * 
	 */
//	@Transactional
//	public void save(LcdPageDescription page) {
//		page.dynamicData = page.dynamicData.toString();
//		page.staticData = page.staticData.toString();
//		// TODO statement.setString(4, page.getStatic().toString().replaceAll("true",
//		// "1").replaceAll("false", "0")); // Speicherplatz
//		page.saveOrUpdate();
//	}

	/**
	 * Gibt die ID der Seite zurück
	 * 
	 * @param jnRoot
	 */
	@Transactional
	public Integer save(String jnRoot) {
		JSONObject jo = new JSONObject(jnRoot);
		LcdPageDescription page = new LcdPageDescription();
		if (jo.has("id")) {
			page.id = jo.getInt("id");
		}
		page.name = jo.getString("name");
		page.timeoutTime = jo.getInt("timeoutTime");

		if (jo.has("staticData")) {
			JSONArray sD = jo.getJSONArray("staticData");
			if (!sD.isEmpty()) {
				page.staticData = sD.toString();
			}
		}

		if (jo.has("dynamicData")) {
			JSONArray dD = jo.getJSONArray("dynamicData");
			if (!dD.isEmpty()) {
				page.dynamicData = dD.toString();
			}
		}

		page.saveOrUpdate();
		return page.id;
	}

	/**
	 * @return
	 * 
	 */
	public LcdPageDescription load(Integer nId) {
		return LcdPageDescription.findById(nId);
	}

	/*
	 * @param nId
	 * 
	 */
	@Transactional
	public void del(Integer nId) {
		LcdPageDescription.findById(nId).delete();
	}

	/**
	 * @param nId
	 */
	public void select(int nId) {
		converter.setPageDescription(load(nId));
	}

	/**
	 * 
	 */
	public void nextPage() {
		// TODO Auto-generated method stub
		System.err.println("nextPage");
	}

	/**
	 * 
	 */
	public void lastPage() {
		// TODO Auto-generated method stub
		System.err.println("lastPage");
	}

}
