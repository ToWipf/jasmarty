package org.wipf.wipfapp.datatypes;

/**
 * @author wipf Achtung ARR x und y sind vertauscht!
 */
public class LcdPage {

	private char[][] page;
	private String sName;
	private int nId;

	public LcdPage(int nWidh, int nHight) {

		this.page = new char[nHight][nWidh];

		for (int x = 0; x < nWidh; x++) {
			for (int y = 0; y < nHight; y++) {
				this.page[y][x] = ' ';
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
			if (sb.length() != 0) {
				sb.append('\n');
			}
			sb.append(line);
		}
		return sb.toString();
	}

	public int getId() {
		return nId;
	}

	public void setId(int nId) {
		this.nId = nId;
	}

	public void stringToPage(String sInput) {
		String[] sAr = sInput.split("\n", -1);
		int nLine = 0;
		for (String s : sAr) {
			this.page[nLine] = s.toCharArray();
			nLine++;
		}
	}
}
