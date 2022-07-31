package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;
import org.wipf.jasmarty.WipfException;

import io.quarkus.elytron.security.common.BcryptUtil;

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
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("username", this.sUsername);
		jo.put("password", this.sPassword);
		jo.put("role", this.sRole);
		jo.put("telegramid", this.nTelegramId);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 * @throws WipfException
	 */
	public WipfUser setByJson(String sJson) throws WipfException {
		JSONObject jo = new JSONObject(sJson);
		String sUsername = jo.getString("username").trim();
		if (sUsername.contains(" ")) {
			throw new WipfException("Keine Leerzeichen im Benutzernamen erlaubt");
		}
		setUsername(sUsername);
		// Mit bcrypt Verschluesselung (slow 32bit)
		setPassword(BcryptUtil.bcryptHash(jo.getString("password")));
		// setPassword(jo.getString("password"));
		setRole(jo.getString("role"));
		setTelegramId(jo.getInt("telegramid"));
		return this;
	}

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
