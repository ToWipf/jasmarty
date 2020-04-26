package org.wipf.jasmarty.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wipf
 */
public class LcdPage {

	private List<String> saLines;
	private String sName;
	private String sOptions;
	private int nId;

	public LcdPage() {
		this.saLines = new ArrayList<String>();
	}

	/**
	 * @return
	 */
	public String toJson() {
		JSONObject jo = new JSONObject();
		jo.put("name", sName);
		jo.put("id", nId);
		// jo.put("options", sOptions);
		jo.put("lines", linesToJson());
		return jo.toString();
	}

	/**
	 * @return
	 */
	private JSONArray linesToJson() {
		JSONArray ja = new JSONArray();

		int nLine = 0;
		for (String line : saLines) {
			JSONObject jo = new JSONObject();

			jo.put("line", nLine);
			jo.put("data", line);
			jo.put("option", sOptions.charAt(nLine));
			ja.put(jo);
			nLine++;
		}
		return ja;
	}

	/**
	 * @param jnRoot
	 * @return
	 */
	public LcdPage setByJson(String jnRoot) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn;
			jn = mapper.readTree(jnRoot);

			this.sName = jn.get("name").asText();
			this.nId = jn.get("id").asInt();
			sOptions = "";

			for (JsonNode jLine : jn.get("lines")) {
				sOptions = sOptions + " ";
				int nLine = jLine.get("line").asInt();
				saLines.add(nLine, jLine.get("data").asText());
				StringBuilder sb = new StringBuilder(sOptions);
				sb.setCharAt(nLine, (char) jLine.get("option").asInt());
				sOptions = sb.toString();
			}
			return this;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public String getPageAsDBString() {
		StringBuilder sb = new StringBuilder();
		for (String line : saLines) {
			if (sb.length() != 0) {
				sb.append('\n');
			}
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * @param sInput
	 */
	public void setStringToPage(String sInput) {
		String[] sAr = sInput.split("\n", -1);
		int nLine = 0;
		for (String s : sAr) {
			this.saLines.add(nLine, s);
			nLine++;
		}
	}

	/**
	 * @param nLine
	 * @return
	 */
	public String getLine(int nLine) {
		try {
			return this.saLines.get(nLine);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param nLine
	 * @param sLine
	 */
	public void setLine(int nLine, String sLine) {
		this.saLines.add(nLine, sLine);
	}

	public int getId() {
		return nId;
	}

	public void setId(int nId) {
		this.nId = nId;
	}

	public String getName() {
		return sName;
	}

	public void setName(String sPagename) {
		this.sName = sPagename;
	}

	public String getOptions() {
		return sOptions;
	}

	public void setOptions(String sOptions) {
		this.sOptions = sOptions;
	}

}
