package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class ButtonAction {

	private int nId;
	private int nButton;
	private boolean bActive;
	private String sAction;

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("button", this.nButton);
		jo.put("active", this.bActive);
		jo.put("action", this.sAction);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return
	 */
	public ButtonAction setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		this.nId = jo.getInt("id");
		this.nButton = jo.getInt("button");
		this.bActive = jo.getBoolean("active");
		this.sAction = jo.getString("action");
		return this;
	}

	/**
	 * @return
	 */
	public int getId() {
		return nId;
	}

	/**
	 * @param nId
	 */
	public void setId(int nId) {
		this.nId = nId;
	}

	/**
	 * @return
	 */
	public int getButton() {
		return nButton;
	}

	/**
	 * @param nButton
	 */
	public void setButton(int nButton) {
		this.nButton = nButton;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return bActive;
	}

	/**
	 * @param bActive
	 */
	public void setActive(boolean bActive) {
		this.bActive = bActive;
	}

	/**
	 * @return
	 */
	public String getAction() {
		return sAction;
	}

	/**
	 * @param sAction
	 */
	public void setAction(String sAction) {
		this.sAction = sAction;
	}

}
