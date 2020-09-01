package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class CustomChar {

	private int nId;
	private int nPosition;
	private String sName;
	private String sData;

	public CustomChar() {
		this.sData = "00000000";
	}

	/**
	 * @param sJson
	 * @return
	 */
	public CustomChar setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);
		this.nId = jo.getInt("id");
		this.sName = jo.getString("name");
		this.nPosition = jo.getInt("position");
		this.sData = jo.getString("data");
		return this;
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
	 * @param b0
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @param nLine
	 */
	public void setLine(boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, int nLine) {
		int n = 0;
		if (b0) {
			n = n + 16;
		}
		if (b1) {
			n = n + 8;
		}
		if (b2) {
			n = n + 4;
		}
		if (b3) {
			n = n + 2;
		}
		if (b4) {
			n = n + 1;
		}
		this.setLine((char) n, nLine);
	}

	/**
	 * @param cData
	 * @param nLine
	 */
	public void setLine(char cData, int nLine) {
		StringBuilder sb = new StringBuilder(sData);
		sb.setCharAt(nLine, cData);
		this.sData = sb.toString();
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
