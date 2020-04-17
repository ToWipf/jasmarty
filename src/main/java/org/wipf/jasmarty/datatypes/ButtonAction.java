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

	public int getId() {
		return nId;
	}

	public void setId(int nId) {
		this.nId = nId;
	}

	public int getButton() {
		return nButton;
	}

	public void setButton(int nButton) {
		this.nButton = nButton;
	}

	public boolean isActive() {
		return bActive;
	}

	public void setActive(boolean bActive) {
		this.bActive = bActive;
	}

	public String getAction() {
		return sAction;
	}

	public void setAction(String sAction) {
		this.sAction = sAction;
	}

}
