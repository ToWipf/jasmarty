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
	 * @param gd
	 */
	public void setFull(GlowiData[][] gd) {
		this.cache = gd;
		listOfChanges.clear();
		for (int x = 0; x < GlowiService.SIZE; x++) {
			for (int y = 0; y < GlowiService.SIZE; y++) {
				listOfChanges.put(kodrToID(x, y), this.cache[x][y]);
			}
		}
	}

	/**
	 * Alle Pixel auf default
	 */
	public void cls() {
		listOfChanges.clear();
		for (int x = 0; x < GlowiService.SIZE; x++) {
			for (int y = 0; y < GlowiService.SIZE; y++) {
				GlowiData m = new GlowiData();
				m.funktion = "N";
				m.farbe_R = 0;
				m.farbe_G = 0;
				m.farbe_B = 0;
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
		listOfChanges.put(kodrToID(x, y), m);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private Integer kodrToID(int x, int y) {
		if (x % 2 == 0) {
			return y + x * GlowiService.SIZE;
		} else {
			return y + x * GlowiService.SIZE + GlowiService.SIZE - y - y - 1;
		}
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
	 * @param y0
	 * @param y1
	 * @param x
	 * @param m
	 */
	public void drawLineV(int y0, int y1, int x, GlowiData m) {
		if (y1 > y0)
			for (int xx = y0; xx <= y1; xx++)
				this.setByXY(xx, x, m);
		else
			for (int xx = y1; xx <= y0; xx++)
				this.setByXY(xx, x, m);
	}

	/**
	 * @param y
	 * @param x0
	 * @param x1
	 * @param m
	 */
	public void drawLineH(int y, int x0, int x1, GlowiData m) {
		if (x1 > x0)
			for (int yy = x0; yy <= x1; yy++)
				this.setByXY(y, yy, m);
		else
			for (int yy = x1; yy <= x0; yy++)
				this.setByXY(y, yy, m);
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
			drawLineV(x, x + w - 1, i, m);
	}

}
