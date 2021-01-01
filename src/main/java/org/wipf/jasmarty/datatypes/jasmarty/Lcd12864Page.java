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
	 * ein volles boolean array (alles hintereinander)
	 * 
	 * @return
	 */
	public boolean[][] getScreenAsBooleanArray() {
		boolean[] btmp = new boolean[8192];
		int nZaeler = 0;
		for (byte bZahl : this.nScreen) {

			int nDez = bZahl;

			for (int i = 0; i < 8; i++) {
				btmp[Math.abs(i - 7) + (nZaeler * 8)] = (nDez % 2 == 1 ? true : false);
				nDez = nDez / 2;
			}
			nZaeler++;
		}

		boolean[][] ba = new boolean[64][128];
		int nLauf = 0;
		int nRow = -1; // bei -1 anfangen
		for (boolean b : btmp) {
			int nCol = nLauf % 128;
			if (nCol == 0) {
				nRow++; // geht von 0 bis 63
			}

			ba[nRow][nCol] = b;
			nLauf++; // von 0 bis 8192
		}
		return ba;
	}

	/**
	 * @return
	 */
	public JSONArray getScreenAsJsonArray() {
		boolean[][] ba = getScreenAsBooleanArray();
		JSONArray ja = new JSONArray();

		for (int r = 0; r < 64; r++) {
			JSONArray jaRow = new JSONArray();
			for (int c = 0; c < 128; c++) {
				jaRow.put(ba[r][c]);
			}
			ja.put(jaRow);
		}
		return ja;
	}

}
