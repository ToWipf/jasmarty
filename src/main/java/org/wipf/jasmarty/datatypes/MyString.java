package org.wipf.jasmarty.datatypes;

import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
public class MyString {
	private String s;

	@Inject
	Wipf wipf;

	public MyString(String s) {
		this.s = wipf.escapeStringSaveCode(s);
	}

	/**
	 * @return
	 */
	public String get() {
		return s;
	}

	/**
	 * @param s
	 */
	public void set(String s) {
		this.s = wipf.escapeStringSaveCode(s);
	}

}
