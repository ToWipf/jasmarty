package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class MyString {
	public static final String REGEX_WHITE = "^[a-zA-z 0-9öäü?!().;.\\-_+*ß]+$";
	public static final String REGEX_BLACK = "^[#]+$";

	private String s;

	/**
	 * @param s
	 */
	public MyString(String s) {
		setBlacklist(s);
	}

	/**
	 * @return
	 */
	public String getS() {
		return s;
	}

	/**
	 * @param s
	 */
	public void setWhitelist(String s) {
		if (s.matches(REGEX_WHITE))
			this.s = s;
		else
			this.s = "fail";
	}

	/**
	 * @param s
	 */
	public void setBlacklist(String s) {
		if (s.matches(REGEX_BLACK))
			this.s = "fail";
		else
			this.s = s;
	}

}