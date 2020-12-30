package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864Cache {

	public static int SIZE = 1024;
	private byte[] baScreen = new byte[SIZE];
	private boolean bChanged = true;

	/**
	 * @return
	 */
	public byte[] getBaScreen() {
		return baScreen;
	}

	/**
	 * Beim Schreiben changed true setzen
	 * 
	 * @param baScreen
	 */
	public void setBaScreen(byte[] baScreen) {
		this.bChanged = true;
		this.baScreen = baScreen;
	}

	/**
	 * @return
	 */
	public boolean isChanged() {
		return bChanged;
	}

	/**
	 * @param bChanged
	 */
	public void setChanged(boolean bChanged) {
		this.bChanged = bChanged;
	}

	/**
	 * @param sJson
	 */
	public void setBaScreen(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		JSONArray a = (JSONArray) jo.get("screen");
		int n = 0;
		for (Object oA : a) {
			this.baScreen[n] = (byte) ((int) oA);
			n++;
		}

	}

}
