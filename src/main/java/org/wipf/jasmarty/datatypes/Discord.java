package org.wipf.jasmarty.datatypes;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Wipf
 *
 */
public class Discord {

	private String sDisId;
	private List<String> lUser = new LinkedList<String>();;
	private Boolean bValid = false;

	/**
	 * @return
	 */
	public String getDisId() {
		return sDisId;
	}

	/**
	 * @param sDisId
	 */
	public void setDisId(String sDisId) {
		this.sDisId = sDisId;
	}

	/**
	 * @return
	 */
	public Boolean isValid() {
		return bValid;
	}

	/**
	 * @param bValid
	 */
	public void setValid(Boolean bValid) {
		this.bValid = bValid;
	}

	/**
	 * @return
	 */
	public List<String> getUser() {
		return lUser;
	}

	/**
	 * @param lUser
	 */
	public void setUser(List<String> lUser) {
		this.lUser = lUser;
	}

	/**
	 * @param sUser
	 */
	public void addUser(String sUser) {
		this.lUser.add(sUser);
	}

	/**
	 * @return
	 */
	public Integer countUser() {
		return this.lUser.size();
	}

	/**
	 * @return
	 */
	public String userToString() {
		StringBuilder sb = new StringBuilder();
		for (String s : getUser()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(s);
		}
		return sb.toString();
	}

}
