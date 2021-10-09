package org.wipf.jasmarty.datatypes.wipfapp;

/**
 * @author Wipf
 *
 */
public class PunktePlay {

	private boolean n0 = false;
	private boolean n1 = false;
	private boolean n2 = false;
	private boolean n3 = false;
	private boolean n4 = false;
	private boolean n5 = false;
	private boolean n6 = false;
	private boolean n7 = false;
	private boolean n8 = false;
	private boolean n9 = false;

	/**
	 * @param sInput
	 */
	public PunktePlay(String sInput) {
		n0 = (sInput.contains("0"));
		n1 = (sInput.contains("1"));
		n2 = (sInput.contains("2"));
		n3 = (sInput.contains("3"));
		n4 = (sInput.contains("4"));
		n5 = (sInput.contains("5"));
		n6 = (sInput.contains("6"));
		n7 = (sInput.contains("7"));
		n8 = (sInput.contains("8"));
		n9 = (sInput.contains("9"));
	}

	/**
	 * @param n
	 * @return
	 */
	public boolean getZahl(int n) {
		switch (n) {
		case 0:
			return n0;
		case 1:
			return n1;
		case 2:
			return n2;
		case 3:
			return n3;
		case 4:
			return n4;
		case 5:
			return n5;
		case 6:
			return n6;
		case 7:
			return n7;
		case 8:
			return n8;
		case 9:
			return n9;

		default:
			return false;
		}
	}

	/**
	 * @param pZuVerleichen
	 * @return
	 */
	public Integer vergleiche(PunktePlay pZuVerleichen) {
		Integer nTreffer = 0;

		for (int i = 0; i <= 9; i++) {
			if (this.getZahl(i) && pZuVerleichen.getZahl(i)) {
				nTreffer++;
			}
		}

		return nTreffer;
	}

}
