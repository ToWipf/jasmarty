package org.wipf.jasmarty.datatypes;

/**
 * @author wipf
 *
 */
public class WipfUser {
	private String sUsername;
	private String sPassword;
	private String sRole;
	private Integer nTelegramId;

	/**
	 * @return
	 */
	public Integer getTelegramId() {
		return nTelegramId;
	}

	/**
	 * @param nTelegramId
	 */
	public void setTelegramId(Integer nTelegramId) {
		this.nTelegramId = nTelegramId;
	}

	/**
	 * @return
	 */
	public String getRole() {
		return sRole;
	}

	/**
	 * @param sRole
	 */
	public void setRole(String sRole) {
		this.sRole = sRole;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return sPassword;
	}

	/**
	 * @param sPassword
	 */
	public void setPassword(String sPassword) {
		this.sPassword = sPassword;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return sUsername;
	}

	/**
	 * @param sUsername
	 */
	public void setUsername(String sUsername) {
		this.sUsername = sUsername;
	}
}
