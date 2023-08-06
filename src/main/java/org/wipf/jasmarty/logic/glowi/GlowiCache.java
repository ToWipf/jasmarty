package org.wipf.jasmarty.logic.glowi;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.glowi.GlowiData;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class GlowiCache {

	private GlowiData[][] cache;
	private Map<Integer, GlowiData> listOfChanges;

	/**
	 * 
	 */
	@PostConstruct
	public void initCache() {
		this.cache = new GlowiData[GlowiService.SIZE][GlowiService.SIZE];
		this.listOfChanges = new HashMap<>();
		cls();
	}

	/**
	 * Alle Pixel auf default
	 */
	public void cls() {
		for (int x = 0; x < GlowiService.SIZE; x++) {
			for (int y = 0; y < GlowiService.SIZE; y++) {
				GlowiData m = new GlowiData();
				m.funktion = "N";
				m.farbe_R = 1;
				m.farbe_G = 1;
				m.farbe_B = 1;
				setByXY(x, y, m);
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public GlowiData getByXY(int x, int y) {
		if (x < GlowiService.SIZE && y < GlowiService.SIZE) {
			return cache[x][y];
		}
		return null;
	}

	/**
	 * @param x
	 * @param y
	 * @param m
	 */
	public void setByXY(int x, int y, GlowiData m) {
		this.cache[x][y] = new GlowiData(m);
		listOfChanges.put(y + x * GlowiService.SIZE, m);
	}

	/**
	 * @return
	 */
	public Map<Integer, GlowiData> getChanges() {
		Map<Integer, GlowiData> tmp = new HashMap<>();
		tmp.putAll(listOfChanges);
		listOfChanges.clear();
		return tmp;
	}

	/**
	 * @return
	 */
	public GlowiData[][] getCache() {
		return cache;
	}

	////////////////////////////////////////////////////////////////
	/////////////////////////// DRAW ///////////////////////////////
	////////////////////////////////////////////////////////////////

	/**
	 * @param x0
	 * @param x1
	 * @param y
	 * @param m
	 */
	public void drawLineH(int x0, int x1, int y, GlowiData m) {
		if (x1 > x0)
			for (int x = x0; x <= x1; x++)
				this.setByXY(x, y, m);
		else
			for (int x = x1; x <= x0; x++)
				this.setByXY(x, y, m);
	}

	/**
	 * @param x
	 * @param y0
	 * @param y1
	 * @param m
	 */
	public void drawLineV(int x, int y0, int y1, GlowiData m) {
		if (y1 > y0)
			for (int y = y0; y <= y1; y++)
				this.setByXY(x, y, m);
		else
			for (int y = y1; y <= y0; y++)
				this.setByXY(x, y, m);
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param m
	 */
	public void drawRectFill(int x, int y, int w, int h, GlowiData m) {
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
