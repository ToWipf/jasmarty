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

	public int getWidh() {
		return nWidh;
	}

	public int getHight() {
		return nHight;
	}

	public char getCacheSoll(int x, int y) {
		return cacheSoll[x][y];
	}

	public char getCacheIst(int x, int y) {
		return cacheIst[x][y];
	}

	public void setToCacheIst(int x, int y, char c) {
		cacheIst[x][y] = c;
	}

	public boolean hasChanges() {
		return (!(this.cacheIst.equals(this.cacheSoll)));
	}

//	public boolean hasChangesInLine(int nLine) {
//		return (!(this.cacheNew[nLine].equals(this.cacheOld[nLine])));
//	}

	public void write(int x, int y, char c) {
		this.cacheSoll[x][y] = c;
	}

	public void writeLine(Integer x, Integer y, char[] cArr) {
		int nOffset = 0;
		for (char c : cArr) {
			write(x + nOffset, y, c);
			nOffset++;
		}

	}

	public String toStringIst() {
		return arrToString(this.cacheIst);
	}

	public String toStringSoll() {
		return arrToString(this.cacheSoll);
	}

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

}
