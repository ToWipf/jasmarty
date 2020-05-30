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
	private String sName;
	private char[] aChar;

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
			this.setLine(0, (char) jn.get("part0").asInt());
			this.setLine(1, (char) jn.get("part1").asInt());
			this.setLine(2, (char) jn.get("part2").asInt());
			this.setLine(3, (char) jn.get("part3").asInt());
			this.setLine(4, (char) jn.get("part4").asInt());
			this.setLine(5, (char) jn.get("part5").asInt());
			this.setLine(6, (char) jn.get("part6").asInt());
			this.setLine(7, (char) jn.get("part7").asInt());
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
		jo.put("part0", this.getLine(0));
		jo.put("part1", this.getLine(1));
		jo.put("part2", this.getLine(2));
		jo.put("part3", this.getLine(3));
		jo.put("part4", this.getLine(4));
		jo.put("part5", this.getLine(5));
		jo.put("part6", this.getLine(6));
		jo.put("part7", this.getLine(7));
		return jo.toString();
	}

	/**
	 * 
	 */
	public CustomChar() {
		this.aChar = new char[8];

		for (int x = 0; x < 8; x++) {
			this.aChar[x] = 0;
		}
	}

	/**
	 * @param nLine
	 * @return
	 */
	public char getLine(int nLine) {
		return this.aChar[nLine];
	}

	/**
	 * @param nLine
	 * @param aChar
	 */
	public void setLine(int nLine, char aChar) {
		this.aChar[nLine] = aChar;
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

}
