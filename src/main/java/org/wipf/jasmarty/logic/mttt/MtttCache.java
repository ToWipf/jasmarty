package org.wipf.jasmarty.logic.mttt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.mttt.mtttData;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MtttCache {

	private mtttData[][] cache;

	/**
	 * 
	 */
	@PostConstruct
	public void initCache() {
		this.cache = new mtttData[MtttService.SIZE][MtttService.SIZE];

		for (int x = 0; x < MtttService.SIZE; x++) {
			for (int y = 0; y < MtttService.SIZE; y++) {
				this.cache[x][y] = new mtttData();
				this.cache[x][y].funktion = "N";
				this.cache[x][y].farbe_R = 9;
				this.cache[x][y].farbe_G = 20;
				this.cache[x][y].farbe_B = 10;
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public mtttData getByXY(int x, int y) {
		if (x < MtttService.SIZE && y < MtttService.SIZE) {
			return cache[x][y];
		}
		return null;
	}

	/**
	 * @return
	 */
	public String toDataString() {
		StringBuilder sb = new StringBuilder();

		for (int x = 0; x < MtttService.SIZE; x++) {
			for (int y = 0; y < MtttService.SIZE; y++) {
				int koordinatenIndex = (y + x * MtttService.SIZE);

				mtttData val = getByXY(x, y);

				sb.append(String.format("%03d", koordinatenIndex)); // Kein unsigned byte oder char in Java :(
				sb.append((char) val.farbe_R);
				sb.append((char) val.farbe_G);
				sb.append((char) val.farbe_B);
			}
		}

		return sb.toString();
	}

	/**
	 * @return
	 */
	public mtttData[][] getCacheForApi() {

//		for (mtttData[] x : this.cache) {
//			for (mtttData y : x) {
//				y.farbe_R = (int) (y.farbe_R);
//				y.farbe_G = (int) (y.farbe_G);
//				y.farbe_B = (int) (y.farbe_B);
//			}
//		}

		return cache;
	}

}
