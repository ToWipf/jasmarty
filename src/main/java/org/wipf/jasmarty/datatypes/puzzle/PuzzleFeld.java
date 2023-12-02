package org.wipf.jasmarty.datatypes.puzzle;

/**
 * @author wipf
 *
 */
public class PuzzleFeld {

	private PuzzleTeil[][] teile;

	/**
	 * @param x
	 * @param y
	 */
	public void setSize(int x, int y) {
		teile = new PuzzleTeil[x][y];

		for (int xx = 0; xx < x; xx++) {
			for (int yy = 0; yy < y; yy++) {
				teile[xx][yy] = new PuzzleTeil();
			}
		}
	}

	/**
	 * @return
	 */
	public PuzzleTeil[][] getTeile() {
		return teile;
	}

	/**
	 * @param x
	 * @param y
	 * @param t
	 */
	public void addTeil(int x, int y, PuzzleTeil t) {
		teile[x][y] = t;
	}

}
