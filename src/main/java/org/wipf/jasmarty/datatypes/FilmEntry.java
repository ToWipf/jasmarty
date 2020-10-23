package org.wipf.jasmarty.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class FilmEntry extends Telegram {

	private Integer nId;
	private MyString sTitel;
	private MyString sArt;
	private Integer nGesehenDate;
	private MyString sInfotext;
	private Integer nBewertung;
	private MyString sEditBy;

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
	public MyString getInfotext() {
		return sInfotext;
	}

	/**
	 * @param sInfotext
	 */
	public void setInfotext(String sInfotext) {
		this.sInfotext = new MyString(sInfotext);
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
	public MyString getArt() {
		return sArt;
	}

	/**
	 * @param sArt
	 */
	public void setArt(String sArt) {
		this.sArt = new MyString(sArt);
	}

	/**
	 * @return
	 */
	public MyString getTitel() {
		return sTitel;
	}

	/**
	 * @param sTitel
	 */
	public void setTitel(String sTitel) {
		this.sTitel = new MyString(sTitel);
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
	public MyString getEditBy() {
		return sEditBy;
	}

	/**
	 * @param sEditBy
	 */
	public void setEditBy(String sEditBy) {
		this.sEditBy = new MyString(sEditBy);
	}

}
