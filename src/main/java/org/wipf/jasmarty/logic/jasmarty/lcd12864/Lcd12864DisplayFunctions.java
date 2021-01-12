package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Lcd12864DisplayFunctions {

	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	Lcd12864FontStyle68 font68;
	@Inject
	Lcd12864FontStyle57 font57;
	@Inject
	Wipf wipf;

	public enum lineAlignment {
		LEFT, CENTER, RIGHT, CUSTOM;
	};

	/**
	 * @param lp
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public Lcd12864Page drawLine(Lcd12864Page lp, int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		int err = dx - dy;

		while (true) {
			lp.setPixel(x0, y0, true);
			if (x0 == x1 && y0 == y1) {
				return lp;
			}
			int err2 = err + err;
			if (err2 > -dy) {
				err -= dy;
				x0 += sx;
			}
			if (err2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	/**
	 * @param lp
	 * @param x0
	 * @param x1
	 * @param y
	 * @return
	 */
	public Lcd12864Page drawLineH(Lcd12864Page lp, int x0, int x1, int y) {
		if (x1 > x0)
			for (int x = x0; x <= x1; x++)
				lp.setPixel(x, y, true);
		else
			for (int x = x1; x <= x0; x++)
				lp.setPixel(x, y, true);
		return lp;
	}

	/**
	 * @param x
	 * @param y0
	 * @param y1
	 * @param col
	 * @return
	 */
	public Lcd12864Page drawLineV(Lcd12864Page lp, int x, int y0, int y1) {
		if (y1 > y0)
			for (int y = y0; y <= y1; y++)
				lp.setPixel(x, y, true);
		else
			for (int y = y1; y <= y0; y++)
				lp.setPixel(x, y, true);
		return lp;
	}

	/**
	 * @param lp
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public Lcd12864Page drawRect(Lcd12864Page lp, int x, int y, int w, int h) {
		if (x >= 128 || y >= 64)
			return lp;
		boolean drawVright = true;
		if (x + w > 128) {
			w = 128 - x;
			drawVright = false;
		}
		if (y + h > 64) {
			h = 64 - y;
		} else {
			drawLineH(lp, x, x + w - 1, y + h - 1);
		}

		drawLineH(lp, x, x + w - 1, y);
		drawLineV(lp, x, y + 1, y + h - 2);

		if (drawVright) {
			drawLineV(lp, x + w - 1, y + 1, y + h - 2);
		}
		return lp;
	}

	/**
	 * @param ld
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public Lcd12864Page drawRectFill(Lcd12864Page ld, int x, int y, int w, int h) {
		if (x >= 128 || y >= 64)
			return ld;
		if (x + w >= 128)
			w = 128 - x;
		if (y + h >= 64)
			h = 64 - y;
		for (int i = y; i < y + h; i++)
			drawLineH(ld, x, x + w - 1, i);
		return ld;
	}

	/**
	 * @param ld
	 * @param x0
	 * @param y0
	 * @param radius
	 */
	public void drawCircle(Lcd12864Page ld, int x0, int y0, int radius) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		ld.setPixel(x0, y0 + radius, true);
		ld.setPixel(x0, y0 - radius, true);
		ld.setPixel(x0 + radius, y0, true);
		ld.setPixel(x0 - radius, y0, true);

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;
			ld.setPixel(x0 + x, y0 + y, true);
			ld.setPixel(x0 - x, y0 + y, true);
			ld.setPixel(x0 + x, y0 - y, true);
			ld.setPixel(x0 - x, y0 - y, true);
			ld.setPixel(x0 + y, y0 + x, true);
			ld.setPixel(x0 - y, y0 + x, true);
			ld.setPixel(x0 + y, y0 - x, true);
			ld.setPixel(x0 - y, y0 - x, true);
		}
	}

	/**
	 * @param ld
	 * @param x0
	 * @param y0
	 * @param r
	 * @return
	 */
	public Lcd12864Page drawCircleFill(Lcd12864Page ld, int x0, int y0, int r) {
		drawLineH(ld, x0 - r, x0 - r + 2 * r + 1, y0);
		int f = 1 - r;
		int ddF_x = 1;
		int ddF_y = -2 * r;
		int x = 0;
		int y = r;

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;
			drawLineH(ld, x0 - x, x0 - x + 2 * x + 1, y0 + y);
			drawLineH(ld, x0 - y, x0 - y + 2 * y + 1, y0 + x);
			drawLineH(ld, x0 - x, x0 - x + 2 * x + 1, y0 - y);
			drawLineH(ld, x0 - y, x0 - y + 2 * y + 1, y0 - x);
		}
		return ld;
	}

	/**
	 * Ein bis zu 8 Pixel hohes Zeichen schreiben
	 * 
	 * @param ld
	 * @param xpos
	 * @param ypos
	 * @param zeichen
	 * @return
	 */
	public Lcd12864Page drawChar(Lcd12864Page ld, int xpos, int ypos, byte[] zeichen) {
		if (xpos >= 128 || ypos >= 64) {
			return ld;
		}

		int x = 0;
		int y = 0;
		for (byte zline : zeichen) {
			y = 0;
			for (boolean bb : wipf.booleanArrayFromByte(zline)) {
				ld.setPixel(x + xpos, y + ypos, bb);
				y++;
			}
			x++;
		}
		return ld;
	}

	/**
	 * TODO zusammenfassen
	 * 
	 * @param ld
	 * @param xpos wird nur bei CUSTOM beachtet
	 * @param ypos
	 * @param str
	 * @return
	 */
	public Lcd12864Page drawStr68(Lcd12864Page ld, Integer xpos, int ypos, lineAlignment la, String str) {
		int x = 0;
		int y = ypos;

		switch (la) {
		case CENTER:
			x = ((128 - (str.length() * font68.getFont1X)) / 2) - font68.getFont1X; // TODO anpassen
			break;
		case RIGHT:
			x = 128 - font68.getFont1X;
			break;
		case LEFT:
			x = 0; // left
			break;
		case CUSTOM:
		default:
			x = xpos;
			break;
		}

		for (char c : str.toCharArray()) {
			byte[] bChar = font68.getChar(c);
			ld = drawChar(ld, x, y, bChar);

			// Zeichenbreite bestimmen
			x += bChar.length + 1;

			// Für Zeilenumbruch -> nötig?
			if (x >= 128 - font68.getFont1X) {
				x = 0;
				y += font68.getFont1Y + 1;
				if (y > 64) {
					y = 0;
				}
			}
		}

		return ld;
	}

	/**
	 * TODO zusammenfassen
	 * 
	 * @param ld
	 * @param xpos wird nur bei CUSTOM beachtet
	 * @param ypos
	 * @param str
	 * @return
	 */
	public Lcd12864Page drawStr57(Lcd12864Page ld, Integer xpos, int ypos, lineAlignment la, String str) {
		int x = 0;
		int y = ypos;

		switch (la) {
		case CENTER:
			x = ((128 - (str.length() * font57.getFont1X)) / 2) - font57.getFont1X; // TODO anpassen
			break;
		case RIGHT:
			x = 128 - font57.getFont1X;
			break;
		case LEFT:
			x = 0; // left
			break;
		case CUSTOM:
		default:
			x = xpos;
			break;
		}

		for (char c : str.toCharArray()) {
			byte[] bChar = font57.getChar(c);
			ld = drawChar(ld, x, y, bChar);

			// Zeichenbreite bestimmen
			x += bChar.length + 1;

			// Für Zeilenumbruch -> nötig?
			if (x >= 128 - font57.getFont1X) {
				x = 0;
				y += font57.getFont1Y + 1;
				if (y > 64) {
					y = 0;
				}
			}
		}

		return ld;
	}

}
