package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class Lcd2004Cache {

	private int nHeight;
	private int nWidth;
	private char[][] cacheIst;
	private char[][] cacheSoll;
	private int nScrollState[];

	/**
	 * @param nWidth
	 * @param nHeight
	 */
	public Lcd2004Cache(int nWidth, int nHeight) {
		this.nWidth = nWidth;
		this.nHeight = nHeight;

		// this.nWidth = nWidth;
		// this.nHeight = nHeight;
		this.cacheIst = new char[nWidth][nHeight];
		this.cacheSoll = new char[nWidth][nHeight];
		this.nScrollState = new int[nHeight];

		for (int y = 0; y < nHeight; y++) {
			for (int x = 0; x < nWidth; x++) {
				this.cacheIst[x][y] = ' ';
				this.cacheSoll[x][y] = ' ';
			}
			this.nScrollState[y] = 0;
		}
	}

	/**
	 * @param nLine
	 * @return
	 */
	public int getScrollStateForLine(int nLine) {
		return this.nScrollState[nLine];
	}

	/**
	 * @param nLine
	 * @param nState
	 */
	public void setScrollStateForLine(int nLine, int nState) {
		this.nScrollState[nLine] = nState;
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
	 * @param nLine
	 * @return
	 */
	public char[] getCacheSollLine(int nLine) {
		char[] ca = new char[nWidth];
		for (int x = 0; x < nWidth; x++) {
			ca[x] = this.cacheSoll[x][nLine];
		}
		return ca;
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
				char c = (this.cacheSoll[x][y]);

				switch (c) {
				case 0x02:
					c = '░';
					break;
				case 0x03:
					c = '▓';
					break;
				case 0x00:
				case 0xFF:
					c = '█';
					break;
				case 0x01:
				case 0x04:
				case 0x05:
				case 0x06:
				case 0x07:
				case 0x08:
				case 0x09:
					c = '?';
					break;
        			default:
            				break;
				}
				sb.append(c);
			}
			jLine.put("line", y);
			jLine.put("data", sb.toString());
			ja.put(jLine);
		}
		jo.put("display", ja);
		jo.put("height", this.nHeight);
		// jo.put("width", this.nWidth); // nicht nötig
		return jo;
	}

	/**
	 * @return
	 * 
	 *         wird derzeit nicht verglichen
	 */
	public JSONObject toIstJson() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (int y = 0; y < this.nHeight; y++) {

			JSONObject jLine = new JSONObject();
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < nWidth; x++) {
				sb.append(this.cacheIst[x][y]);
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
