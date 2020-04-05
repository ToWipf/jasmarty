package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class LcdCache {

	private int nHight;
	private int nWidh;
	private char[][] cacheIst;
	private char[][] cacheSoll;

	/**
	 * @param nWidh
	 * @param nHight
	 */
	public LcdCache(int nWidh, int nHight) {
		this.nWidh = nWidh;
		this.nHight = nHight;

		// this.nWidh = nWidh;
		// this.nHight = nHight;
		this.cacheIst = new char[nWidh][nHight];
		this.cacheSoll = new char[nWidh][nHight];

		for (int x = 0; x < nWidh; x++) {
			for (int y = 0; y < nHight; y++) {
				this.cacheIst[x][y] = ' ';
				this.cacheSoll[x][y] = ' ';
			}
		}
	}

	/**
	 * @return
	 */
	public int getWidh() {
		return nWidh;
	}

	/**
	 * @return
	 */
	public int getHight() {
		return nHight;
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
	 * @param arr
	 * @return
	 */
	private String arrToString(char[][] arr) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < nHight; y++) {
			sb.append("\n");
			sb.append("Line: " + y + ": ");
			for (int x = 0; x < nWidh; x++) {
				sb.append(arr[x][y]);
			}
		}
		return sb.toString();
	}

	/**
	 * Leert cache Ist und soll
	 */
	public void clearCacheFull() {
		for (int x = 0; x < nWidh; x++) {
			for (int y = 0; y < nHight; y++) {
				this.cacheSoll[x][y] = ' ';
				this.cacheIst[x][y] = ' ';
			}
		}
	}

}
