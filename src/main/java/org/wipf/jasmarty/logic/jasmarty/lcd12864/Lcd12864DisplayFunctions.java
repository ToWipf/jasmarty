package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;

/**
 * @author Wipf
 * 
 *         Alles was nicht auf true gesetzt wird ist false
 *
 */
@ApplicationScoped
public class Lcd12864DisplayFunctions {

	@Inject
	Lcd12864Cache lcd12864Cache;

	/**
	 * 
	 */
	public void cls() {
		lcd12864Cache.setScreen(new Lcd12864Page());
	}

	/**
	 * @param x
	 * @param y
	 * @param col
	 */
	public void setPixel(int x, int y) {
		if (x > 0 && y > 0 && -x <= 128 && y <= 64) {
			Lcd12864Page ls = lcd12864Cache.getScreen();
			boolean[][] ba = ls.getScreenAsBooleanArray();
			ba[y][x] = true;
			ls.setScreen(ba);
			lcd12864Cache.setScreen(ls);
		}
	}

	/**
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param col
	 */
	void drawLine(int x0, int y0, int x1, int y1) {
		Lcd12864Page ls = lcd12864Cache.getScreen();
		boolean[][] ba = ls.getScreenAsBooleanArray();

		int dx = x1 - x0;
		int dy = y1 - y0;
		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		int err = dx - dy;

		while (true) {
			ba[x0][y0] = true;
			if (x0 == x1 && y0 == y1) {

				ls.setScreen(ba);
				lcd12864Cache.setScreen(ls);
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

//	/**
//	 * @param x0
//	 * @param x1
//	 * @param y
//	 * @param col
//	 */
//	public void drawLineH(int x0, int x1, int y, int col) {
//		if (x1 > x0)
//			for (int x = x0; x <= x1; x++)
//				drawPixel(x, y, col);
//		else
//			for (int x = x1; x <= x0; x++)
//				drawPixel(x, y, col);
//	}
//
//	/**
//	 * @param x
//	 * @param y0
//	 * @param y1
//	 * @param col
//	 */
//	void drawLineV(int x, int y0, int y1, int col) {
//		if (y1 > y0)
//			for (int y = y0; y <= y1; y++)
//				drawPixel(x, y, col);
//		else
//			for (int y = y1; y <= y0; y++)
//				drawPixel(x, y, col);
//	}
//
//	/**
//	 * @param x
//	 * @param y
//	 * @param w
//	 * @param h
//	 * @param col
//	 */
//	public void drawRect(int x, int y, int w, int h, int col) {
//		if (x >= SCR_WD || y >= SCR_HT)
//			return;
//		byte drawVright = 1;
//		if (x + w > SCR_WD) {
//			w = SCR_WD - x;
//			drawVright = 0;
//		}
//		if (y + h > SCR_HT)
//			h = SCR_HT - y;
//		else
//			drawLineHfast(x, x + w - 1, y + h - 1, col);
//		drawLineHfast(x, x + w - 1, y, col);
//		drawLineVfast(x, y + 1, y + h - 2, col);
//		if (drawVright)
//			drawLineVfast(x + w - 1, y + 1, y + h - 2, col);
//	}
//
//	// ----------------------------------------------------------------
//	// dithered version (50% of brightness)
//	/**
//	 * @param x
//	 * @param y
//	 * @param w
//	 * @param h
//	 * @param col
//	 */
//	public void drawRectD(int x, int y, int w, int h, int col) {
//		if (x >= SCR_WD || y >= SCR_HT)
//			return;
//		byte drawVright = 1;
//		if (x + w > SCR_WD) {
//			w = SCR_WD - x;
//			drawVright = 0;
//		}
//		if (y + h > SCR_HT)
//			h = SCR_HT - y;
//		else
//			drawLineHfastD(x, x + w - 1, y + h - 1, col);
//		drawLineHfastD(x, x + w - 1, y, col);
//		drawLineVfastD(x, y + 1, y + h - 2, col);
//		if (drawVright)
//			drawLineVfastD(x + w - 1, y + 1, y + h - 2, col);
//	}
//
//	/**
//	 * @param x
//	 * @param y
//	 * @param w
//	 * @param h
//	 * @param col
//	 */
//	public void fillRect(int x, int y, int w, int h, int col) {
//		if (x >= SCR_WD || y >= SCR_HT)
//			return;
//		if (x + w > SCR_WD)
//			w = SCR_WD - x;
//		if (y + h > SCR_HT)
//			h = SCR_HT - y;
//		for (int i = y; i < y + h; i++)
//			drawLineHfast(x, x + w - 1, i, col);
//	}
//
//	// ----------------------------------------------------------------
//	// dithered version (50% of brightness)
//	/**
//	 * @param x
//	 * @param y
//	 * @param w
//	 * @param h
//	 * @param col
//	 */
//	public void fillRectD(int x, int y, int w, int h, int col) {
//		if (x >= SCR_WD || y >= SCR_HT)
//			return;
//		if (x + w >= SCR_WD)
//			w = SCR_WD - x;
//		if (y + h >= SCR_HT)
//			h = SCR_HT - y;
//		for (int i = y; i < y + h; i++)
//			drawLineHfastD(x, x + w - 1, i, col);
//	}
//
//	// ----------------------------------------------------------------
//	// circle
//	/**
//	 * @param x0
//	 * @param y0
//	 * @param radius
//	 * @param col
//	 */
//	public void drawCircle(int x0, int y0, int radius, int col) {
//		int f = 1 - radius;
//		int ddF_x = 1;
//		int ddF_y = -2 * radius;
//		int x = 0;
//		int y = radius;
//
//		drawPixel(x0, y0 + radius, col);
//		drawPixel(x0, y0 - radius, col);
//		drawPixel(x0 + radius, y0, col);
//		drawPixel(x0 - radius, y0, col);
//
//		while (x < y) {
//			if (f >= 0) {
//				y--;
//				ddF_y += 2;
//				f += ddF_y;
//			}
//			x++;
//			ddF_x += 2;
//			f += ddF_x;
//			drawPixel(x0 + x, y0 + y, col);
//			drawPixel(x0 - x, y0 + y, col);
//			drawPixel(x0 + x, y0 - y, col);
//			drawPixel(x0 - x, y0 - y, col);
//			drawPixel(x0 + y, y0 + x, col);
//			drawPixel(x0 - y, y0 + x, col);
//			drawPixel(x0 + y, y0 - x, col);
//			drawPixel(x0 - y, y0 - x, col);
//		}
//	}
//
//	/**
//	 * @param x0
//	 * @param y0
//	 * @param r
//	 * @param col
//	 */
//	void fillCircle(int x0, int y0, int r, int col) {
//		drawLineHfast(x0 - r, x0 - r + 2 * r + 1, y0, col);
//		int16_t f = 1 - r;
//		int16_t ddF_x = 1;
//		int16_t ddF_y = -2 * r;
//		int16_t x = 0;
//		int16_t y = r;
//
//		while (x < y) {
//			if (f >= 0) {
//				y--;
//				ddF_y += 2;
//				f += ddF_y;
//			}
//			x++;
//			ddF_x += 2;
//			f += ddF_x;
//			drawLineHfast(x0 - x, x0 - x + 2 * x + 1, y0 + y, col);
//			drawLineHfast(x0 - y, x0 - y + 2 * y + 1, y0 + x, col);
//			drawLineHfast(x0 - x, x0 - x + 2 * x + 1, y0 - y, col);
//			drawLineHfast(x0 - y, x0 - y + 2 * y + 1, y0 - x, col);
//		}
//	}
//
//	// ----------------------------------------------------------------
//	// dithered version (50% of brightness)
//	/**
//	 * @param x0
//	 * @param y0
//	 * @param r
//	 * @param col
//	 */
//	public void fillCircleD(int x0, int y0, int r, int col) {
//		drawLineHfastD(x0 - r, x0 - r + 2 * r + 1, y0, col);
//		int16_t f = 1 - r;
//		int16_t ddF_x = 1;
//		int16_t ddF_y = -2 * r;
//		int16_t x = 0;
//		int16_t y = r;
//
//		while (x < y) {
//			if (f >= 0) {
//				y--;
//				ddF_y += 2;
//				f += ddF_y;
//			}
//			x++;
//			ddF_x += 2;
//			f += ddF_x;
//			drawLineHfastD(x0 - x, x0 - x + 2 * x + 1, y0 + y, col);
//			drawLineHfastD(x0 - y, x0 - y + 2 * y + 1, y0 + x, col);
//			drawLineHfastD(x0 - x, x0 - x + 2 * x + 1, y0 - y, col);
//			drawLineHfastD(x0 - y, x0 - y + 2 * y + 1, y0 - x, col);
//		}
//	}
//
//	/**
//	 * @param s
//	 */
//	public void setDither(int s) {
//		if (s > 16)
//			return;
//		pattern[0] = pgm_read_byte(ditherTab + s * 4 + 0);
//		pattern[1] = pgm_read_byte(ditherTab + s * 4 + 1);
//		pattern[2] = pgm_read_byte(ditherTab + s * 4 + 2);
//		pattern[3] = pgm_read_byte(ditherTab + s * 4 + 3);
//	}
//
//	/**
//	 * @return
//	 */
//	public int drawBitmap(const int *bmp, int x, int y, int w, int h)
//	{
//	  int wdb = w;
//	  ALIGNMENT;
//	  byte i,j8,d,b,ht8=(h+7)/8;
//	  for(j8=0; j8<ht8; j8++) {
//	    for(i=0; i<w; i++) {
//	      byte mask = 0x80 >> ((x+i)&7);
//	      d = pgm_read_byte(bmp+wdb*j8+i);
//	      int lastbit = h - j8 * 8;
//	      if (lastbit > 8) lastbit = 8;
//	      for(b=0; b<lastbit; b++) {
//	         if(d & 1) scr[(y+j8*8+b)*scrWd+(x+i)/8] |= mask;
//	         d>>=1;
//	      }
//	    }
//	  }
//	  return x+w;
//	}
//
//	/**
//	 * @return
//	 */
//	int drawBitmap(const int *bmp, int x, int y)
//	{
//	  int w = pgm_read_byte(bmp+0);
//	  int h = pgm_read_byte(bmp+1);
//	  return drawBitmap(bmp+2, x, y, w, h);
//	}
//
//	// ----------------------------------------------------------------
//	// text rendering
//	// ----------------------------------------------------------------
//	/**
//	 * 
//	 */
//	void setFont(const int* font)
//	{
//	  cfont.font = font;
//	  cfont.xSize = fontbyte(0);
//	  cfont.ySize = fontbyte(1);
//	  cfont.firstCh = fontbyte(2);
//	  cfont.lastCh  = fontbyte(3);
//	  cfont.minDigitWd = 0;
//	  cfont.minCharWd = 0;
//	  isNumberFun = &isNumber;
//	  spacing = 1;
//	  cr = 0;
//	  invertCh = 0;
//	}
//
//	/**
//	 * @param c
//	 * @param last
//	 * @return
//	 */
//	int charWidth(int c, bool last)
//	{
//	  c = convertPolish(c);
//	  if (c < cfont.firstCh || c > cfont.lastCh)
//	      return c==' ' ?  1 + cfont.xSize/2 : 0;
//	  if (cfont.xSize > 0) return cfont.xSize;
//	  int ys8=(cfont.ySize+7)/8;
//	  int idx = 4 + (c-cfont.firstCh)*(-cfont.xSize*ys8+1);
//	  int wd = pgm_read_byte(cfont.font + idx);
//	  int wdL = 0, wdR = spacing; // default spacing before and behind char
//	  if((*isNumberFun)(c)) {
//	    if(cfont.minDigitWd>wd) {
//	      wdL = (cfont.minDigitWd-wd)/2;
//	      wdR += (cfont.minDigitWd-wd-wdL);
//	    }
//	  } else
//	  if(cfont.minCharWd>wd) {
//	    wdL = (cfont.minCharWd-wd)/2;
//	    wdR += (cfont.minCharWd-wd-wdL);
//	  }
//	  return last ? wd+wdL+wdR : wd+wdL+wdR-spacing;  // last!=0 -> get rid of last empty columns 
//	}

///**
// * @return
// */
//public int strWidth(char *str)
//	{
//	  int wd = 0;
//	  while (*str) wd += charWidth(*str++);
//	  return wd;
//	}
//	// ----------------------------------------------------------------
//	int ST7920_SPI::printChar(int xpos, int ypos, unsigned char c)
//	{
//	  if(xpos >= SCR_WD || ypos >= SCR_HT)  return 0;
//	  int fht8 = (cfont.ySize + 7) / 8, wd, fwd = cfont.xSize;
//	  if(fwd < 0)  fwd = -fwd;
//
//	  c = convertPolish(c);
//	  if(c < cfont.firstCh || c > cfont.lastCh)  return c==' ' ?  1 + fwd/2 : 0;
//
//	  int x,y8,b,cdata = (c - cfont.firstCh) * (fwd*fht8+1) + 4;
//	  byte d;
//	  wd = fontbyte(cdata++);
//	  int wdL = 0, wdR = spacing;
//	  if((*isNumberFun)(c)) {
//	    if(cfont.minDigitWd>wd) {
//	      wdL = (cfont.minDigitWd-wd)/2;
//	      wdR += (cfont.minDigitWd-wd-wdL);
//	    }
//	  } else
//	  if(cfont.minCharWd>wd) {
//	    wdL = (cfont.minCharWd-wd)/2;
//	    wdR += (cfont.minCharWd-wd-wdL);
//	  }
//	  if(xpos+wd+wdL+wdR>SCR_WD) wdR = max(SCR_WD-xpos-wdL-wd, 0);
//	  if(xpos+wd+wdL+wdR>SCR_WD) wd  = max(SCR_WD-xpos-wdL, 0);
//	  if(xpos+wd+wdL+wdR>SCR_WD) wdL = max(SCR_WD-xpos, 0);
//
//	  for(x=0; x<wd; x++) {
//	    byte mask = 0x80 >> ((xpos+x+wdL)&7);
//	    for(y8=0; y8<fht8; y8++) {
//	      d = fontbyte(cdata+x*fht8+y8);
//	      int lastbit = cfont.ySize - y8 * 8;
//	      if (lastbit > 8) lastbit = 8;
//	      for(b=0; b<lastbit; b++) {
//	         if(d & 1) scr[(ypos+y8*8+b)*scrWd+(xpos+x+wdL)/8] |= mask;  //drawPixel(xpos+x, ypos+y8*8+b, 1);
//	         d>>=1;
//	      }
//	    }
//	  }
//	  return wd+wdR+wdL;
//	}
//
//	/**
//	 * @param xpos
//	 * @param ypos
//	 * @return
//	 */
//	public int printStr(int xpos, int ypos, char *str)
//	{
//	  unsigned char ch;
//	  int stl, row;
//	  int x = xpos;
//	  int y = ypos;
//	  int wd = strWidth(str);
//
//	  if(x==-1) // right = -1
//	    x = SCR_WD - wd;
//	  else if(x<0) // center = -2
//	    x = (SCR_WD - wd) / 2;
//	  if(x<0) x = 0; // left
//
//	  while(*str) {
//	    int wd = printChar(x,y,*str++);
//	    x+=wd;
//	    if(cr && x>=SCR_WD) { 
//	      x=0; 
//	      y+=cfont.ySize; 
//	      if(y>SCR_HT) y = 0;
//	    }
//	  }
//	  if(invertCh) fillRect(xpos,x-1,y,y+cfont.ySize+1,2);
//	  return x;
//	}
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

}
