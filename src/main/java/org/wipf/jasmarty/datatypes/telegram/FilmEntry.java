package org.wipf.jasmarty.datatypes.telegram;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class FilmEntry extends Telegram {

	private Integer nId;
	private String sTitel;
	private String sArt;
	private Integer nGesehenDate;
	private String sInfotext;
	private Integer nBewertung;
	private String sEditBy;

	/**
	 * @return
	 */
	@Override
	public JSONObject toJson() {
		JSONObject jo = super.toJson();
		jo.put("id", nId);
		jo.put("titel", sTitel);
		jo.put("art", sArt);
		jo.put("gesehenDate", nGesehenDate);
		jo.put("infotext", sInfotext);
		jo.put("bewertung", nBewertung);
		jo.put("editby", sEditBy);
		return jo;
	}

	/**
	 * @return
	 */
	public JSONObject toJsonRelevantOnly() {
		JSONObject jo = super.toJson();
		jo.put("id", nId);
		jo.put("titel", sTitel);
		jo.put("infotext", sInfotext);
		return jo;
	}

	/**
	 * @param sJson
	 * @return
	 */
	public FilmEntry setByJson(String sJson) {
		this.setByTelegram(this.setByJsonTelegram(sJson));
		JSONObject jo = new JSONObject(sJson);

		this.setId(jo.getInt("id"));
		this.setTitel(jo.getString("titel"));
		this.setArt(jo.getString("art"));
		this.setGesehenDate(jo.getInt("gesehenDate"));
		this.setInfotext(jo.getString("infotext"));
		this.setBewertung(jo.getInt("bewertung"));
		this.setEditBy(jo.getString("editby"));

		return this;
	}

	/**
	 * @return
	 */
	public Integer getBewertung() {
		return nBewertung;
	}

	/**
	 * @param nBewertung
	 */
	public void setBewertung(Integer nBewertung) {
		this.nBewertung = nBewertung;
	}

	/**
	 * @return
	 */
	public String getInfotext() {
		return sInfotext;
	}

	/**
	 * @param sInfotext
	 */
	public void setInfotext(String sInfotext) {
		this.sInfotext = sInfotext;
	}

	/**
	 * @return
	 */
	public Integer getGesehenDate() {
		return nGesehenDate;
	}

	/**
	 * @param nGesehenDate
	 */
	public void setGesehenDate(Integer nGesehenDate) {
		this.nGesehenDate = nGesehenDate;
	}

	/**
	 * @return
	 */
	public String getArt() {
		return sArt;
	}

	/**
	 * @param sArt
	 */
	public void setArt(String sArt) {
		this.sArt = sArt;
	}

	/**
	 * @return
	 */
	public String getTitel() {
		return sTitel;
	}

	/**
	 * @param sTitel
	 */
	public void setTitel(String sTitel) {
		this.sTitel = sTitel;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return nId;
	}

	/**
	 * @param nId
	 */
	public void setId(Integer nId) {
		this.nId = nId;
	}

	/**
	 * @return
	 */
	public String getEditBy() {
		return sEditBy;
	}

	/**
	 * @param sEditBy
	 */
	public void setEditBy(String sEditBy) {
		this.sEditBy = sEditBy;
	}

}