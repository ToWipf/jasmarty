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
		int[] iScreen = new int[1024];
		JSONArray a = new JSONArray(sJson);

		int cLine = 0;
		int cPosInLine = 0;
		for (Object o : a) {
			// Eine Zeile
			JSONArray line = (JSONArray) o;

			for (Object by : line) {
				// Eine Stelle
				boolean b = (boolean) by;

				if (b) {

					System.out.println("ROW: " + cLine + " COL: " + cPosInLine);
					// Get full byte of 8
					int nRun = cPosInLine % 8;
					int curretInt = (cPosInLine % 32) * cLine;
					// 32

					iScreen[curretInt] = iScreen[curretInt] + 2 ^ nRun;

					// Übertagen 7654321

					// TODO Add do iScreen
				}
				cPosInLine++;
			}
			cPosInLine = 0;
			cLine++;
		}

	}

//			System.out.println("True bei ");
//
//			// boolean[] ba = [];
//			int nbyte = 0;
//
//			for (int i = 0; i < line.length(); i++) {
//				// Sollte immer 8192 laufen
//				boolean b = line.getBoolean(i);
//
//				if (b) {
//					int localbyte = (nbyte % 8); // Von 0 bis 7
//					System.out.println("TRUE BEI " + i + " | " + localbyte);
//					iScreen[i] = (1 * localbyte + 1);
//				}
//				nbyte++;
//			}
//
//			// byte[] baLine = new byte[line.length() / 8];
//
//			// for (int nlc = 0; nlc < line.length() / 8; nlc++) {
//			// line.get(nlc);
//			// }
//
//			// byte[] x = toBytes(line);
//		}
//
//		for (int nx : iScreen) {
//			// System.out.println(nx);
//		}

	/**
	 * @param input
	 * @return
	 */
	private byte[] toBytes(boolean[] input) {
		byte[] toReturn = new byte[input.length / 8];
		for (int entry = 0; entry < toReturn.length; entry++) {
			for (int bit = 0; bit < 8; bit++) {
				if (input[entry * 8 + bit]) {
					toReturn[entry] |= (128 >> bit);
				}
			}
		}

		return toReturn;
	}

	/**
	 * @param input
	 * @return
	 */
	private byte[] toBytes(JSONArray input) {
		byte[] toReturn = new byte[input.length() / 8];
		for (int entry = 0; entry < toReturn.length; entry++) {
			for (int bit = 0; bit < 8; bit++) {
				if (input.getBoolean(entry * 8 + bit)) {
					toReturn[entry] |= (128 >> bit);
				}
			}
		}

		return toReturn;
	}

}
