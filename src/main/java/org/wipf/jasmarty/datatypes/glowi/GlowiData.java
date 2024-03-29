package org.wipf.jasmarty.datatypes.glowi;

/**
 * @author wipf
 *
 */
public class GlowiData {

	public int farbe_R;
	public int farbe_G;
	public int farbe_B;
	public String funktion = "";

	public enum farbe {
		ROT, GRUEN, BLAU, GRAU, GELB, SCHWARZ, AUS
	};

	public GlowiData() {
		this.farbe_R = 0;
		this.farbe_G = 0;
		this.farbe_B = 0;
		this.funktion = "";
	}

	public GlowiData(GlowiData copy) {
		this.farbe_R = copy.farbe_R;
		this.farbe_G = copy.farbe_G;
		this.farbe_B = copy.farbe_B;
		this.funktion = copy.funktion;
	}

	/**
	 * @param f
	 */
	public void setFarbe(farbe f) {
		switch (f) {
		case ROT:
			farbe_R = 30;
			farbe_G = 0;
			farbe_B = 0;
			break;
		case GRUEN:
			farbe_R = 0;
			farbe_G = 30;
			farbe_B = 0;
			break;
		case BLAU:
			farbe_R = 0;
			farbe_G = 0;
			farbe_B = 30;
			break;
		case GRAU:
			farbe_R = 30;
			farbe_G = 30;
			farbe_B = 30;
			break;
		case GELB:
			farbe_R = 30;
			farbe_G = 30;
			farbe_B = 0;
			break;
		case SCHWARZ:
			farbe_R = 4;
			farbe_G = 4;
			farbe_B = 3;
			break;
		case AUS:
			farbe_R = 0;
			farbe_G = 0;
			farbe_B = 0;
			break;

		default:
			break;
		}

	}

}
