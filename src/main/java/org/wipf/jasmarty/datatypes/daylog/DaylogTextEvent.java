package org.wipf.jasmarty.datatypes.daylog;

/**
 * @author Wipf
 *
 */
public class DaylogTextEvent {

	private Integer nId;
	private Integer nDateId;
	private String sTyp;
	private String sText;

	public String getTyp() {
		return sTyp;
	}

	public void setTyp(String sTyp) {
		this.sTyp = sTyp;
	}

	public String getText() {
		return sText;
	}

	public void setText(String sText) {
		this.sText = sText;
	}

	public Integer getDateId() {
		return nDateId;
	}

	public void setDateId(Integer nDateId) {
		this.nDateId = nDateId;
	}

	public Integer getId() {
		return nId;
	}

	public void setId(Integer nId) {
		this.nId = nId;
	}

}
