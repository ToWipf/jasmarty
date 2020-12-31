package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

import org.json.JSONArray;

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
	 * @param iScreen
	 */
	public void setBaScreen(int[] iScreen) {
		this.bChanged = true;

		for (int i = 0; i < 1024; i++) {
			this.baScreen[i] = (byte) iScreen[i];

		}
		this.bChanged = true;
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
		JSONArray a = new JSONArray(sJson);
		boolean[] baTmpFull = new boolean[8192];

		int cTmp = 0;
		for (Object o : a) {
			JSONArray line = (JSONArray) o;

			for (Object by : line) {
				boolean b = (boolean) by;
				baTmpFull[cTmp] = b;
				cTmp++;
			}
		}

		int[] iScreen = new int[1024];
		for (int i = 0; i < 1024; i++) {
			int stellenstart = i * 8;
			int tmpzahl = 0;

			for (int lauf = 0; lauf < 8; lauf++) {
				if (baTmpFull[stellenstart + lauf]) {
					// System.out.println("Truestelle " + stellenstart + " p " + lauf);
					tmpzahl = (int) (tmpzahl + Math.pow(2, Math.abs(lauf - 7)));
				}
			}

			iScreen[i] = tmpzahl;
		}

		setBaScreen(iScreen);
	}

}
