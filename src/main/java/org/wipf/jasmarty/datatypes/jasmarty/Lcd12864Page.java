package org.wipf.jasmarty.datatypes.jasmarty;

/**
 * @author wipf
 *
 */
public class Lcd12864Page extends Lcd12864PageBase {

	public enum lineAlignment {
		LEFT, CENTER, RIGHT, CUSTOM;
	};

	/**
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	public void drawLine(int x0, int y0, int x1, int y1, pixelType pt) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		int err = dx - dy;

		while (true) {
			this.setPixel(x0, y0, pt);
			if (x0 == x1 && y0 == y1) {
				return;
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

	public void drawLineH(int x0, int x1, int y, pixelType pt) {
		if (x1 > x0)
			for (int x = x0; x <= x1; x++)
				this.setPixel(x, y, pt);
		else
			for (int x = x1; x <= x0; x++)
				this.setPixel(x, y, pt);
	}

	/**
	 * @param x
	 * @param y0
	 * @param y1
	 */
	public void drawLineV(int x, int y0, int y1, pixelType pt) {
		if (y1 > y0)
			for (int y = y0; y <= y1; y++)
				this.setPixel(x, y, pt);
		else
			for (int y = y1; y <= y0; y++)
				this.setPixel(x, y, pt);
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawRect(int x, int y, int w, int h, pixelType pt) {
		if (x >= 128 || y >= 64)
			return;
		boolean drawVright = true;
		if (x + w > 128) {
			w = 128 - x;
			drawVright = false;
		}
		if (y + h > 64) {
			h = 64 - y;
		} else {
			drawLineH(x, x + w - 1, y + h - 1, pt);
		}

		drawLineH(x, x + w - 1, y, pt);
		drawLineV(x, y + 1, y + h - 2, pt);

		if (drawVright) {
			drawLineV(x + w - 1, y + 1, y + h - 2, pt);
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawRectFill(int x, int y, int w, int h, pixelType pt) {
		if (x >= 128 || y >= 64)
			return;
		if (x + w >= 128)
			w = 128 - x;
		if (y + h >= 64)
			h = 64 - y;
		for (int i = y; i < y + h; i++)
			drawLineH(x, x + w - 1, i, pt);
	}

	/**
	 * @param x0
	 * @param y0
	 * @param radius
	 */
	public void drawCircle(int x0, int y0, int radius, pixelType pt) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		this.setPixel(x0, y0 + radius, pt);
		this.setPixel(x0, y0 - radius, pt);
		this.setPixel(x0 + radius, y0, pt);
		this.setPixel(x0 - radius, y0, pt);

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;
			this.setPixel(x0 + x, y0 + y, pt);
			this.setPixel(x0 - x, y0 + y, pt);
			this.setPixel(x0 + x, y0 - y, pt);
			this.setPixel(x0 - x, y0 - y, pt);
			this.setPixel(x0 + y, y0 + x, pt);
			this.setPixel(x0 - y, y0 + x, pt);
			this.setPixel(x0 + y, y0 - x, pt);
			this.setPixel(x0 - y, y0 - x, pt);
		}
	}

	/**
	 * @param x0
	 * @param y0
	 * @param r
	 */
	public void drawCircleFill(int x0, int y0, int r, pixelType pt) {
		drawLineH(x0 - r, x0 - r + 2 * r + 1, y0, pt);
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
			drawLineH(x0 - x, x0 - x + 2 * x + 1, y0 + y, pt);
			drawLineH(x0 - y, x0 - y + 2 * y + 1, y0 + x, pt);
			drawLineH(x0 - x, x0 - x + 2 * x + 1, y0 - y, pt);
			drawLineH(x0 - y, x0 - y + 2 * y + 1, y0 - x, pt);
		}
	}

	/**
	 * Ein bis zu 8 Pixel hohes Zeichen schreiben
	 *
	 * @param xpos
	 * @param ypos
	 * @param zeichen
	 */
	public void drawChar(int xpos, int ypos, byte[] zeichen, pixelType pt) {
		if (xpos >= 128 || ypos >= 64) {
			return;
		}

		int x = 0;
		int y = 0;
		for (byte zline : zeichen) {
			y = 0;
			for (boolean bb : booleanArrayFromByte(zline)) {
				if (bb) {
					// Nur Pixel die auch relevant sind bearbeiten
					this.setPixel(x + xpos, y + ypos, pt);
				}
				y++;
			}
			x++;
		}
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
	public void drawString(Lcd12864Font font, Integer xpos, int ypos, lineAlignment la, String str, pixelType pt) {
		int x = 0;
		int y = ypos;

		switch (la) {
		case CENTER:
			x = ((128 - (str.length() * font.getFontX()) / 2) - font.getFontX()); // TODO anpassen
			break;
		case RIGHT:
			x = 128 - font.getFontX();
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
			byte[] bChar = font.getChar(c);
			drawChar(x, y, bChar, pt);

			// Zeichenbreite bestimmen
			x += bChar.length + 1;

			// Für Zeilenumbruch -> nötig?
			if (x >= 128 - font.getFontX()) {
				x = 0;
				y += font.getFontY() + 1;
				if (y > 64) {
					y = 0;
				}
			}
		}
	}

	/**
	 * @param x
	 * @return
	 */
	private boolean[] booleanArrayFromByte(byte x) {
		boolean bs[] = new boolean[8];
		bs[0] = ((x & 0x01) != 0);
		bs[1] = ((x & 0x02) != 0);
		bs[2] = ((x & 0x04) != 0);
		bs[3] = ((x & 0x08) != 0);
		bs[4] = ((x & 0x10) != 0);
		bs[5] = ((x & 0x20) != 0);
		bs[6] = ((x & 0x40) != 0);
		bs[7] = ((x & 0x80) != 0);
		return bs;
	}

}
