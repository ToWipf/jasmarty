package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONArray;

/**
 * @author Wipf
 *
 */
public class Lcd12864Page {

	private boolean[][] baScreen = new boolean[64][128];

	/**
	 * 
	 */
	public Lcd12864Page() {

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
	public Lcd12864Page(byte[] nScreen) {
		setScreen(nScreen);
	}

	/**
	 * @param baScreen
	 */
	public Lcd12864Page(boolean[][] baScreen) {
		setScreen(baScreen);
	}

	/**
	 * baScreenList 1024
	 * 
	 * @return
	 */
	public void setScreen(byte[] baScreenList) {
		boolean[] bTmp = new boolean[8192];
		int nZaeler = 0;
		for (byte bZahl : baScreenList) {

			// unsigned byte
			int nDez = bZahl & 0xFF;

			for (int i = 0; i < 8; i++) {
				bTmp[Math.abs(i - 7) + (nZaeler * 8)] = (nDez % 2 == 1 ? true : false);
				nDez = nDez / 2;
			}
			nZaeler++;
		}
		setScreen(bTmp);
	}

	/**
	 * 8192 lange liste
	 * 
	 * @param baScreenList
	 */
	public void setScreen(boolean[] baScreenList) {

		int nLauf = 0;
		int nRow = -1; // bei -1 anfangen
		for (boolean b : baScreenList) {
			int nCol = nLauf % 128;
			if (nCol == 0) {
				nRow++; // geht von 0 bis 63
			}

			this.baScreen[nRow][nCol] = b;
			nLauf++; // von 0 bis 8192
		}
	}

	/**
	 * @param baScreen
	 */
	public void setScreen(boolean[][] baScreen) {
		this.baScreen = baScreen;
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
		setScreen(baTmpFull);
	}

	/**
	 * @param x
	 * @param y
	 * @param b
	 */
	public void setPixel(int x, int y, boolean b) {
		if (x >= 0 && x < 128 && y >= 0 && y < 64) {
			this.baScreen[y][x] = b;
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setPixelInvert(int x, int y) {
		setPixel(x, y, !getPixel(x, y));

	}

	/**
	 * @return
	 */
	public boolean[][] getScreen() {
		return this.baScreen;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean getPixel(int x, int y) {
		if (x >= 0 && x < 128 && y >= 0 && y < 64) {
			return this.baScreen[x][y];
		}
		return false;
	}

	/**
	 * @return
	 * 
	 */
	public byte[] getScreenAsByteArray() {
		boolean[] baTmpFull = getScreenAsBooleanArryInLine();

		// für get als byte liste
		byte[] iScreen = new byte[1024];
		for (int i = 0; i < 1024; i++) {
			int stellenstart = i * 8;
			byte tmpzahl = 0;

			for (int lauf = 0; lauf < 8; lauf++) {
				if (baTmpFull[stellenstart + lauf]) {
					// System.out.println("Truestelle " + stellenstart + " p " + lauf);
					tmpzahl = (byte) (tmpzahl + Math.pow(2, Math.abs(lauf - 7)));
				}
			}

			iScreen[i] = tmpzahl;
		}
		return iScreen;
	}

	/**
	 * TODO testen ob 8 bit richtigrum
	 * 
	 * [8192]x
	 * 
	 * @param baScreen
	 */
	public boolean[] getScreenAsBooleanArryInLine() {
		boolean[] ba = new boolean[8192];
		int i = 0;
		for (boolean[] bRow : baScreen) {
			for (boolean b : bRow) {
				ba[i] = b;
				i++;
			}
		}
		return ba;
	}

	/**
	 * @return
	 */
	public JSONArray getScreenAsJsonArray() {
		JSONArray ja = new JSONArray();

		for (int r = 0; r < 64; r++) {
			JSONArray jaRow = new JSONArray();
			for (int c = 0; c < 128; c++) {
				jaRow.put(baScreen[r][c]);
			}
			ja.put(jaRow);
		}
		return ja;
	}

}
