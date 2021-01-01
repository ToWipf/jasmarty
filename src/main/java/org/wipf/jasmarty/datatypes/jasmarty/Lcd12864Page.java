package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONArray;

/**
 * @author Wipf
 *
 */
public class Lcd12864Page {

	public static int SIZE = 1024;
	private byte[] nScreen = new byte[SIZE];

	/**
	 * 
	 */
	public Lcd12864Page() {

	}

	/**
	 * @param nScreen
	 */
	public Lcd12864Page(byte[] nScreen) {
		setScreen(nScreen);
	}

	/**
	 * @param sJson
	 */
	public Lcd12864Page(String sJson) {
		setScreen(sJson);
	}

	/**
	 * @param nScreen
	 */
	public void setScreen(byte[] nScreen) {
		this.nScreen = nScreen;
	}

	/**
	 * @param iScreen
	 */
	public void setScreen(int[] iScreen) {
		for (int i = 0; i < 1024; i++) {
			this.nScreen[i] = (byte) iScreen[i];

		}
	}

	/**
	 * Json:
	 * 
	 * [[true,false,true,...(128x)],[],...(64x)]
	 * 
	 * @param sJson
	 */
	public void setScreen(String sJson) {
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
		setScreen(iScreen);
	}

	/**
	 * @return
	 */
	public byte[] getScreenAsByteArray() {
		return nScreen;
	}

	/**
	 * @return
	 */
	public JSONArray getScreenAsJsonArray() {
		JSONArray ja = new JSONArray();

		boolean[][] ba = new boolean[64][128];

		int nZaeler = 0;
		for (byte bZahl : this.nScreen) {
			int row = nZaeler % 128;

			// nzahler von 0 - 1023
			// bzahl von 0 - 255

			// row von 0 - 63
			// col von 0 - 127

			// jede bZahl in bool mit je 8 zerlegen und er entsprechenden reihen zufügen

			boolean[] baBit = wandleDezInBin(bZahl);

			// entspr. stelle finden und 8 anfügen
			// ba[row] =

			// finde jedes 128 iges um einen bruch zu machen

		}

		/*
		 * for (int y = 0; y < 64; y++) { // eine Reihe -> ein Array JSONArray jaRow =
		 * new JSONArray();
		 * 
		 * // in einer Reihe for (int x = 0; x < 128 / 8; x++) { int xPosStart = x * 8;
		 * 
		 * int relevatZahl = this.nScreen[1]; // Von 0 bis 1024
		 * 
		 * // for (int nr = 0; nr < 8; nr++) { // jaRow.put(this.nScreen[xPosStart + nr]
		 * % 8); // } } ja.put(jaRow); }
		 */
		return ja;
	}

	/**
	 * @param n
	 * @return
	 */
	private boolean[] wandleDezInBin(int n) {
		if (n > 255 || n < 0) {
			return null;
		}

		int nDez = n;
		boolean bin[] = new boolean[8];

		for (int i = 0; i < 8; i++) {
			bin[i] = (nDez % 2 == 1 ? true : false);
			nDez = nDez / 2;
		}

		return bin;
	}

}
