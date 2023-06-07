package org.wipf.jasmarty.logic.lcd;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
	@Transactional
	public void save(LcdPageDescription page) {
		page.dynamicData = page.dynamicData.toString();
		page.staticData = page.staticData.toString();
		// TODO statement.setString(4, page.getStatic().toString().replaceAll("true",
		// "1").replaceAll("false", "0")); // Speicherplatz
		page.saveOrUpdate();
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
