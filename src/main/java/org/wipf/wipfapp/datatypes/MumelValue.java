package org.wipf.wipfapp.datatypes;

/**
 * @author wipf
 *
 */
public class MumelValue {

	private Integer nVal;

	/**
	 * @param mumval
	 */
	MumelValue(Integer mumval) {
		check(mumval);
	}

	/**
	 * 
	 */
	MumelValue() {
		this.nVal = -1;
	}

	/**
	 * @return
	 */
	public Integer getVal() {
		return nVal;
	}

	/**
	 * @param mumval
	 */
	public void setVal(Integer mumval) {
		this.nVal = check(mumval);
	}

	/**
	 * @return
	 */
	public String toValString() {
		if (nVal < 10) {
			return "0" + nVal;
		} else {
			return nVal.toString();
		}
	}

	/**
	 * @param mumval
	 */
	private Integer check(Integer mumval) {
		if (mumval > 0 && mumval < 100) {
			return mumval;
		}
		return null;
	}

}
