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
				this.cache[x][y].farbe_R = 10;
				this.cache[x][y].farbe_G = 10;
				this.cache[x][y].farbe_B = 0;
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
				sb.append(val.farbe_R);
				sb.append(val.farbe_G);
				sb.append(val.farbe_B);
			}
		}

		return sb.toString();
	}

}
