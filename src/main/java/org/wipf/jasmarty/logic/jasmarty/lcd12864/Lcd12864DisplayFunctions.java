package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;

/**
 * @author Wipf
 * 
 *
 */
@ApplicationScoped
public class Lcd12864DisplayFunctions {

	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	Fonts fonts;

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
	 * @param ld
	 * @param xpos
	 * @param ypos
	 * @param c
	 * @return
	 */
	public Lcd12864Page drawChar(Lcd12864Page ld, int xpos, int ypos, char c) {
		if (xpos >= 128 || ypos >= 64) {
			return ld;
		}

		byte[] zeichen = fonts.getFont1(c);

		for (int xc = 0; xc < fonts.getFont1X; xc++) {
			// bauplan laden
			boolean[] zeichenLine = booleanArrayFromByte(zeichen[xc]);

			for (int yc = 0; yc < fonts.getFont1Y; yc++) {

				if (zeichenLine[yc]) {
					ld.setPixel(xc + xpos, yc + ypos, true);
				}
			}
		}
		return ld;
	}

	/**
	 * @param ld
	 * @param xpos
	 * @param ypos
	 * @param str
	 * @return
	 */
	public Lcd12864Page drawStr(Lcd12864Page ld, int xpos, int ypos, String str) {
		int x = xpos;
		int y = ypos;
		int wd = str.length() * 8;

		if (x == -1) // right = -1
			x = 128 - wd;
		else if (x < 0) // center = -2
			x = (128 - wd) / 2;
		if (x < 0)
			x = 0; // left

		for (char c : str.toCharArray()) {
			ld = drawChar(ld, x, y, c);
			x += wd;
			if (x >= 128) {
				x = 0;
				y += fonts.getFont1Y;
				if (y > 64)
					y = 0;
			}
		}

		if (true == true) { // TODO ?
			drawRect(ld, xpos, x - 1, y, y + fonts.getFont1Y + 1);
		}
		return ld;
	}
//
//	/**
//	 * @param pos
//	 */
//	public void printTxt(int pos, char *str)
//	{
//	  sendCmd(LCD_BASIC);
//	  sendCmd(pos);
//	  while(*str) sendData(*str++);
//	}
//
//	/**
//	 * @param pos
//	 */
//	void printTxt(int pos, uint16_t *signs)
//	{
//	  sendCmd(LCD_BASIC);
//	  sendCmd(pos);
//	  while(*signs) {  sendData(*signs>>8); sendData(*signs&0xff); signs++; }
//	}
//
//	/**
//	 * // y = 0..63 -> buffer #0 // y = 64..127 -> buffer #1
//	 * 
//	 * @param x
//	 * @param y
//	 */
//	private void gotoXY(byte x, byte y) {
//		if (y >= 32 && y < 64) {
//			y -= 32;
//			x += 8;
//		} else if (y >= 64 && y < 64 + 32) {
//			y -= 32;
//			x += 0;
//		} else if (y >= 64 + 32 && y < 64 + 64) {
//			y -= 64;
//			x += 8;
//		}
//		sendCmd(LCD_ADDR | y); // 6-bit (0..63)
//		sendCmd(LCD_ADDR | x); // 4-bit (0..15)
//	}

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
