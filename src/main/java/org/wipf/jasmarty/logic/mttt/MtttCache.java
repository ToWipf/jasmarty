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

	public enum modus_type {
		MTTT, NONE
	};

	private mtttData[][] cache;
	public modus_type modus;

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
				this.cache[x][y].farbe_R = 1;
				this.cache[x][y].farbe_G = 1;
				this.cache[x][y].farbe_B = 1;
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
	public mtttData[][] getCache() {
		return cache;
	}

	////////////////////////////////////////////////////////////////

	/**
	 * @param x
	 * @param y
	 * @param pt
	 */
	public void setPixel(int x, int y, mtttData m) {

		this.cache[x][y] = new mtttData(m);
	}

	public void drawLineH(int x0, int x1, int y, mtttData m) {
		if (x1 > x0)
			for (int x = x0; x <= x1; x++)
				this.setPixel(x, y, m);
		else
			for (int x = x1; x <= x0; x++)
				this.setPixel(x, y, m);
	}

	/**
	 * @param x
	 * @param y0
	 * @param y1
	 */
	public void drawLineV(int x, int y0, int y1, mtttData m) {
		if (y1 > y0)
			for (int y = y0; y <= y1; y++)
				this.setPixel(x, y, m);
		else
			for (int y = y1; y <= y0; y++)
				this.setPixel(x, y, m);
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawRectFill(int x, int y, int w, int h, mtttData m) {
		if (x >= 128 || y >= 64)
			return;
		if (x + w >= 128)
			w = 128 - x;
		if (y + h >= 64)
			h = 64 - y;
		for (int i = y; i < y + h; i++)
			drawLineH(x, x + w - 1, i, m);
	}

}
