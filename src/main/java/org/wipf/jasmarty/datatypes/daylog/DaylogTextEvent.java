package org.wipf.jasmarty.datatypes.daylog;

/**
 * @author Wipf
 *
 */
public class DaylogTextEvent {

	private Integer nDateId;
	private String sTyp;
	private String sText;

	public String getsTyp() {
		return sTyp;
	}

	public void setsTyp(String sTyp) {
		this.sTyp = sTyp;
	}

	public String getsText() {
		return sText;
	}

	public void setsText(String sText) {
		this.sText = sText;
	}

	public Integer getnDateId() {
		return nDateId;
	}

	public void setnDateId(Integer nDateId) {
		this.nDateId = nDateId;
	}

}
