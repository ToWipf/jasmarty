package org.wipf.jasmarty.datatypes;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class LcdCache {

	private int nHeight;
	private int nWidth;
	private char[][] cacheIst;
	private char[][] cacheSoll;

	/**
	 * @param nWidth
	 * @param nHeight
	 */
	public LcdCache(int nWidth, int nHeight) {
		this.nWidth = nWidth;
		this.nHeight = nHeight;

		// this.nWidth = nWidth;
		// this.nHeight = nHeight;
		this.cacheIst = new char[nWidth][nHeight];
		this.cacheSoll = new char[nWidth][nHeight];

		for (int x = 0; x < nWidth; x++) {
			for (int y = 0; y < nHeight; y++) {
				this.cacheIst[x][y] = ' ';
				this.cacheSoll[x][y] = ' ';
			}
		}
	}

	/**
	 * Leert cache Ist und soll
	 */
	public void clearCacheFull() {
		for (int x = 0; x < nWidth; x++) {
			for (int y = 0; y < nHeight; y++) {
				this.cacheSoll[x][y] = ' ';
				this.cacheIst[x][y] = ' ';
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public char getCacheSoll(int x, int y) {
		return cacheSoll[x][y];
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public char getCacheIst(int x, int y) {
		return cacheIst[x][y];
	}

	/**
	 * @param x
	 * @param y
	 * @param c
	 */
	public void setToCacheIst(int x, int y, char c) {
		cacheIst[x][y] = c;
	}

	/**
	 * @return
	 */
	public boolean hasChanges() {
		return (!(this.cacheIst.equals(this.cacheSoll)));
	}

//	public boolean hasChangesInLine(int nLine) {
//		return (!(this.cacheNew[nLine].equals(this.cacheOld[nLine])));
//	}

	/**
	 * @param x
	 * @param y
	 * @param c
	 */
	public void write(int x, int y, char c) {
		this.cacheSoll[x][y] = c;
	}

	/**
	 * @param x
	 * @param y
	 * @param cArr
	 */
	public void writeLine(Integer x, Integer y, char[] cArr) {
		int nOffset = 0;
		for (char c : cArr) {
			write(x + nOffset, y, c);
			nOffset++;
		}
	}

	/**
	 * @return
	 */
	public JSONObject toSollJson() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (int y = 0; y < this.nHeight; y++) {

			JSONObject jLine = new JSONObject();
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < nWidth; x++) {
				sb.append(this.cacheSoll[x][y]);
			}

			jLine.put("line", y);
			jLine.put("data", sb.toString());
			ja.put(jLine);
		}
		jo.put("display", ja);
		jo.put("height", this.nHeight);
		jo.put("width", this.nWidth);
		return jo;
	}

	/**
	 * @return
	 */// TODO
	public JSONObject toIstJson() {
		JSONObject jo = new JSONObject();

		for (int y = 0; y < nHeight; y++) {
			StringBuilder sb = new StringBuilder();

			for (int x = 0; x < nWidth; x++) {
				sb.append(this.cacheIst[x][y]);
				jo.put("\"l" + y + "\"", sb.toString());
			}
		}
		return jo;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return nWidth;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return nHeight;
	}

}
