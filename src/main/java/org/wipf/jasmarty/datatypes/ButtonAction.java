package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		jo.put("id", nId);
		jo.put("button", nButton);
		jo.put("active", bActive);
		jo.put("action", sAction);
		return jo.toString();
	}

	/**
	 * @param sJson
	 * @return
	 */
	public ButtonAction setByJson(String sJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sJson);

			this.nId = jn.get("id").asInt();
			this.nButton = jn.get("button").asInt();
			this.bActive = jn.get("active").asBoolean();
			this.sAction = jn.get("action").asText();
			return this;
		} catch (Exception e) {
			return null;
		}
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
