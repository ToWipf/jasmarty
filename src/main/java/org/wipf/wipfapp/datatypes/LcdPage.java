package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class LcdPage {

	private char[][] page;
	private String sName;

	public LcdPage(int nWidh, int nHight) {

		this.page = new char[nWidh][nHight];

		for (int x = 0; x < nWidh; x++) {
			for (int y = 0; y < nHight; y++) {
				this.page[x][y] = ' ';
			}
		}
	}

	public void setPage(char[][] page) {
		this.page = page;
	}

	public String getName() {
		return sName;
	}

	public char[][] getPage() {
		return page;
	}

	public void setName(String sPagename) {
		this.sName = sPagename;
	}

	public char[] getLine(int nLine) {
		return this.page[nLine];
	}

	public void setLine(int nLine, char[] cArr) {
		this.page[nLine] = cArr;
	}

	public String getPageAsDBString() {
		StringBuilder sb = new StringBuilder();
		for (char[] line : page) {
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
}
