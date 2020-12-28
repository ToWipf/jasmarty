package org.wipf.jasmarty.datatypes.jasmarty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author wipf
 */
public class Lcd2004Page {

	private List<String> saLines;
	private String sName = "";
	private String sOptions;
	private int nId;

	/**
	 * 
	 */
	public Lcd2004Page() {
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
	 * @param sJson
	 * @return
	 */
	public Lcd2004Page setByJson(String sJson) {
		JSONObject jo = new JSONObject(sJson);

		this.sName = jo.getString("name");
		this.nId = jo.getInt("id");
		sOptions = ""; // Leer neu setzen

		JSONArray ja = jo.getJSONArray("lines");

		for (int i = 0; i < ja.length(); i++) {
			JSONObject joLine = ja.getJSONObject(i);

			sOptions = sOptions + " "; // TODO nicht sicher -> könnte alles verschieben
			int nLine = joLine.getInt("line");
			saLines.add(nLine, joLine.getString("data"));
			StringBuilder sb = new StringBuilder(sOptions);
			sb.setCharAt(nLine, (char) joLine.getInt("option"));
			sOptions = sb.toString();
		}
		return this;
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
		return this.saLines.get(nLine);
	}

	/**
	 * @param nLine
	 * @param sLine
	 */
	public void setLine(int nLine, String sLine) {
		this.saLines.add(nLine, sLine);
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
	public String getName() {
		return sName;
	}

	/**
	 * @param sPagename
	 */
	public void setName(String sPagename) {
		this.sName = sPagename;
	}

	/**
	 * @return
	 */
	public String getOptions() {
		return sOptions;
	}

	/**
	 * @param sOptions
	 */
	public void setOptions(String sOptions) {
		this.sOptions = sOptions;
	}

}
