package org.wipf.jasmarty.datatypes;

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
	 * @param arr
	 * @return
	 */
	private String arrToString(char[][] arr) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < nHeight; y++) {
			sb.append("\n");
			sb.append("Line: " + y + ": ");
			for (int x = 0; x < nWidth; x++) {
				sb.append(arr[x][y]);
			}
		}
		return sb.toString();
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
	public String toStringIst() {
		return arrToString(this.cacheIst);
	}

	/**
	 * @return
	 */
	public String toStringSoll() {
		return arrToString(this.cacheSoll);
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
