package org.wipf.jasmarty.datatypes.mttt;

/**
 * @author wipf
 *
 */
public class mtttSpieler {

//	public enum erlFeld {
//		F1, F2, F3, F4, F5, F6, F7, F8, F9, ALL
//	};

	public enum werdran {
		SPIELER_X, SPIELER_Y, NONE
	};

	public String erlaubtesNaechstesFeld = "";
	public String letztesFeld = "";
	public werdran werIstDran = werdran.NONE;

}
