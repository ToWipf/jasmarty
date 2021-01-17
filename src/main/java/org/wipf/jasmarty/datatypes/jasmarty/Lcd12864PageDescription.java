package org.wipf.jasmarty.datatypes.jasmarty;

import org.json.JSONArray;

/**
 * @author wipf
 *
 */
public class Lcd12864PageDescription {

	private int nId;
	private String sName;
	private JSONArray jaStatic;
	private JSONArray jaDynamic;

	/**
	 * @return
	 */
	public String getName() {
		return sName;
	}

	/**
	 * @param sName
	 */
	public void setName(String sName) {
		this.sName = sName;
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
	public JSONArray getStatic() {
		return jaStatic;
	}

	/**
	 * @param jaStatic
	 */
	public void setStatic(JSONArray jaStatic) {
		this.jaStatic = jaStatic;
	}

	/**
	 * @param jaStatic
	 */
	public void setStatic(String sStatic) {
		setStatic(new JSONArray(sStatic));
	}

	/**
	 * @return
	 */
	public JSONArray getDynamic() {
		return jaDynamic;
	}

	/**
	 * @param jaDynamic
	 */
	public void setDynamic(JSONArray jaDynamic) {
		this.jaDynamic = jaDynamic;
	}

	/**
	 * @param jaStatic
	 */
	public void setDynamic(String sStatic) {
		setDynamic(new JSONArray(sStatic));
	}

}
