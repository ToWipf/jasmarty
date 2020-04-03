package org.wipf.wipfapp.datatypes;

public class LcdCache {

	private int nHight;
	private int nWidh;
	private char[][] cacheNew;
	private char[][] cacheOld;

	public LcdCache(int nWidh, int nHight) {
		this.nWidh = nWidh;
		this.nHight = nHight;
		this.cacheNew = new char[nWidh][nHight];

		for (int x = 0; x < nWidh; x++) {
			for (int y = 0; y < nHight; y++) {
				this.cacheNew[x][y] = ' ';
			}
		}

		this.cacheOld = this.cacheNew;
	}

	public int getWidh() {
		return nWidh;
	}

	public int getHight() {
		return nHight;
	}

	public char getCacheNew(int x, int y) {
		return cacheNew[x][y];
	}

	public char getCacheOld(int x, int y) {
		return cacheOld[x][y];
	}

	public void setToCacheOld(int x, int y, char c) {
		cacheOld[x][y] = c;
	}

	public boolean hasChanges() {
		return (!(this.cacheNew.equals(this.cacheOld)));
	}

//	public boolean hasChangesInLine(int nLine) {
//		return (!(this.cacheNew[nLine].equals(this.cacheOld[nLine])));
//	}

	public void write(int x, int y, char c) {
		this.cacheNew[x][y] = c;
	}

	public void writeLine(Integer x, Integer y, String sString) {
		int nOffset = 0;
		for (char c : sString.toCharArray()) {
			write(x + nOffset, y, c);
			nOffset++;
		}

	}

}
