package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf
 *
 */
public class CustomChar {

	private int nId;
	private int nPosition;
	private String sName;
	private String sData;

	/**
	 * @param sJson
	 * @return
	 */
	public CustomChar setByJson(String sJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(sJson);

			this.nId = jn.get("id").asInt();
			this.sName = jn.get("name").asText();
			this.nPosition = jn.get("position").asInt();
			this.sData = jn.get("data").asText();
			return this;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.nId);
		jo.put("name", this.sName);
		jo.put("position", this.nPosition);
		jo.put("data", this.sData);
		return jo.toString();
	}

	/**
	 * @param nLine
	 * @return
	 */
	public char getBytesForLine(int nLine) {
		char c = this.sData.charAt(nLine);
		switch (c) {
		// 0 - 9 gehen so
		case 'a':
			return 10;
		case 'b':
			return 11;
		case 'c':
			return 12;
		case 'd':
			return 13;
		case 'e':
			return 14;
		case 'f':
			return 15;
		case 'g':
			return 16;
		case 'h':
			return 17;
		case 'i':
			return 18;
		case 'j':
			return 19;
		case 'k':
			return 20;
		case 'l':
			return 21;
		case 'm':
			return 22;
		case 'n':
			return 23;
		case 'o':
			return 24;
		case 'p':
			return 25;
		case 'q':
			return 26;
		case 'r':
			return 27;
		case 's':
			return 28;
		case 't':
			return 29;
		case 'u':
			return 30;
		case 'v':
			return 31;
		default:
			return c;

		}
	}

	/**
	 * @return
	 */
	public String getName() {
		return sName;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.sName = name;
	}

	/**
	 * @return
	 */
	public int getId() {
		return nId;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.nId = id;
	}

	/**
	 * @return
	 */
	public int getPosition() {
		return nPosition;
	}

	/**
	 * @param nPosition
	 */
	public void setPosition(int nPosition) {
		this.nPosition = nPosition;
	}

	/**
	 * @return
	 */
	public String getData() {
		return sData;
	}

	/**
	 * @param sData
	 */
	public void setData(String sData) {
		this.sData = sData;
	}

}
