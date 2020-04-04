package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class LcdPage {

	private char[][] page;
	private String sName;

	public char[][] getPage() {
		return page;
	}

	public void setPage(char[][] page) {
		this.page = page;
	}

	public String getName() {
		return sName;
	}

	public void setName(String sPagename) {
		this.sName = sPagename;
	}

	public char[] getLine(int nLine) {
		return this.page[nLine];
	}
}
