package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Lcd12864FontStyle57 {

	/**
	 * 
	 */
	public int getFont1X = 5;
	/**
	 * 
	 */
	public int getFont1Y = 7;

	/**
	 * @param c
	 * @return
	 */
	public byte[] getChar(char c) {
		switch (c) {
		case ' ':
			return new byte[] { (byte) 0x00 }; //
		case '!':
			return new byte[] { (byte) 0x5F }; // !
		case '"':
			return new byte[] { (byte) 0x03, (byte) 0x00, (byte) 0x03 }; // "
		case '#':
			return new byte[] { (byte) 0x14, (byte) 0x7F, (byte) 0x14, (byte) 0x7F, (byte) 0x14 }; // #
		case '$':
			return new byte[] { (byte) 0x24, (byte) 0x4A, (byte) 0xFF, (byte) 0x4A, (byte) 0x34 }; // $
		case '%':
			return new byte[] { (byte) 0x61, (byte) 0x1C, (byte) 0x43 }; // %
		case '&':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F }; // & //TODO
		case '\'':
			return new byte[] { (byte) 0x06, (byte) 0x09, (byte) 0x09, (byte) 0x06 }; // '
		case '(':
			return new byte[] { (byte) 0x3E, (byte) 0x41 }; // (
		case ')':
			return new byte[] { (byte) 0x41, (byte) 0x3E }; // )
		case '*':
			return new byte[] { (byte) 0x14, (byte) 0x08, (byte) 0x1C, (byte) 0x08, (byte) 0x14 }; // *
		case '+':
			return new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x3E, (byte) 0x08, (byte) 0x08 }; // +
		case ',':
			return new byte[] { (byte) 0x80, (byte) 0x40 }; // ,
		case '-':
			return new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x08 }; // -
		case '.':
			return new byte[] { (byte) 0x40 }; // .
		case '/':
			return new byte[] { (byte) 0x60, (byte) 0x1C, (byte) 0x03 }; // /
		case '0':
			return new byte[] { (byte) 0x3E, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x3E }; // 0
		case '1':
			return new byte[] { (byte) 0x44, (byte) 0x42, (byte) 0x7F, (byte) 0x40, (byte) 0x40 }; // 1
		case '2':
			return new byte[] { (byte) 0x62, (byte) 0x51, (byte) 0x49, (byte) 0x49, (byte) 0x46 }; // 2
		case '3':
			return new byte[] { (byte) 0x22, (byte) 0x41, (byte) 0x49, (byte) 0x49, (byte) 0x36 }; // 3
		case '4':
			return new byte[] { (byte) 0x18, (byte) 0x14, (byte) 0x12, (byte) 0x7F, (byte) 0x10 }; // 4
		case '5':
			return new byte[] { (byte) 0x4F, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x31 }; // 5
		case '6':
			return new byte[] { (byte) 0x3E, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x32 }; // 6
		case '7':
			return new byte[] { (byte) 0x01, (byte) 0x61, (byte) 0x11, (byte) 0x09, (byte) 0x07 }; // 7
		case '8':
			return new byte[] { (byte) 0x36, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x36 }; // 8
		case '9':
			return new byte[] { (byte) 0x26, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x3E }; // 9
		case ':':
			return new byte[] { (byte) 0x28 }; // :
		case ';':
			return new byte[] { (byte) 0x80, (byte) 0x48, }; // ;
		case '<':
			return new byte[] { (byte) 0x08, (byte) 0x14, (byte) 0x22, (byte) 0x41 }; // <
		case '=':
			return new byte[] { (byte) 0x14, (byte) 0x14, (byte) 0x14, (byte) 0x14 }; // =
		case '>':
			return new byte[] { (byte) 0x41, (byte) 0x22, (byte) 0x14, (byte) 0x08 }; // >
		case '?':
			return new byte[] { (byte) 0x02, (byte) 0x51, (byte) 0x09, (byte) 0x06 }; // ?
		case '@':
			return new byte[] { (byte) 0x3E, (byte) 0x59, (byte) 0x59, (byte) 0x59, (byte) 0x46 }; // @
		case 'A':
			return new byte[] { (byte) 0x7E, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x7E }; // A
		case 'B':
			return new byte[] { (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x36 }; // B
		case 'C':
			return new byte[] { (byte) 0x3E, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x22 }; // C
		case 'D':
			return new byte[] { (byte) 0x7F, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x3E }; // D
		case 'E':
			return new byte[] { (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x41, (byte) 0x41 }; // E
		case 'F':
			return new byte[] { (byte) 0x7F, (byte) 0x09, (byte) 0x09, (byte) 0x01, (byte) 0x01 }; // F
		case 'G':
			return new byte[] { (byte) 0x3E, (byte) 0x41, (byte) 0x49, (byte) 0x49, (byte) 0x3A }; // G
		case 'H':
			return new byte[] { (byte) 0x7F, (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x7F }; // H
		case 'I':
			return new byte[] { (byte) 0x7F }; // I
		case 'J':
			return new byte[] { (byte) 0x21, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x3F }; // J
		case 'K':
			return new byte[] { (byte) 0x7F, (byte) 0x08, (byte) 0x14, (byte) 0x22, (byte) 0x41 }; // K
		case 'L':
			return new byte[] { (byte) 0x7F, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40 }; // L
		case 'M':
			return new byte[] { (byte) 0x7F, (byte) 0x02, (byte) 0x1C, (byte) 0x02, (byte) 0x7F }; // M
		case 'N':
			return new byte[] { (byte) 0x7F, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x7F }; // N
		case 'O':
			return new byte[] { (byte) 0x3E, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x3E }; // O
		case 'P':
			return new byte[] { (byte) 0x7F, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x0E }; // P
		case 'Q':
			return new byte[] { (byte) 0x3E, (byte) 0x41, (byte) 0xC1, (byte) 0xA1, (byte) 0xBE }; // Q
		case 'R':
			return new byte[] { (byte) 0x7F, (byte) 0x11, (byte) 0x11, (byte) 0x29, (byte) 0x46 }; // R
		case 'S':
			return new byte[] { (byte) 0x26, (byte) 0x49, (byte) 0x49, (byte) 0x49, (byte) 0x32 }; // S
		case 'T':
			return new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x7F, (byte) 0x01, (byte) 0x01 }; // T
		case 'U':
			return new byte[] { (byte) 0x3F, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x3F }; // U
		case 'V':
			return new byte[] { (byte) 0x07, (byte) 0x18, (byte) 0x60, (byte) 0x18, (byte) 0x07 }; // V
		case 'W':
			return new byte[] { (byte) 0x1F, (byte) 0x60, (byte) 0x18, (byte) 0x60, (byte) 0x1F }; // W
		case 'X':
			return new byte[] { (byte) 0x63, (byte) 0x14, (byte) 0x08, (byte) 0x14, (byte) 0x63 }; // X
		case 'Y':
			return new byte[] { (byte) 0x03, (byte) 0x0C, (byte) 0x70, (byte) 0x0C, (byte) 0x03 }; // Y
		case 'Z':
			return new byte[] { (byte) 0x61, (byte) 0x51, (byte) 0x49, (byte) 0x45, (byte) 0x43 }; // Z
		case '[':
			return new byte[] { (byte) 0x7F, (byte) 0x41, (byte) 0x41 }; // [
		case '\\':
			return new byte[] { (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20 }; // BackSlash
		case ']':
			return new byte[] { (byte) 0x41, (byte) 0x41, (byte) 0x7F }; // ]
		case '^':
			return new byte[] { (byte) 0x04, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x04 }; // ^
		case '_':
			return new byte[] { (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80 }; // _
		case '`':
			return new byte[] { (byte) 0x01, (byte) 0x02 }; // `
		case 'a':
			return new byte[] { (byte) 0x20, (byte) 0x54, (byte) 0x54, (byte) 0x38, (byte) 0x40 }; // a
		case 'b':
			return new byte[] { (byte) 0x3F, (byte) 0x48, (byte) 0x44, (byte) 0x44, (byte) 0x38 }; // b
		case 'c':
			return new byte[] { (byte) 0x38, (byte) 0x44, (byte) 0x44, (byte) 0x44, (byte) 0x28 }; // c
		case 'd':
			return new byte[] { (byte) 0x38, (byte) 0x44, (byte) 0x44, (byte) 0x28, (byte) 0x7F }; // d
		case 'e':
			return new byte[] { (byte) 0x38, (byte) 0x54, (byte) 0x54, (byte) 0x54, (byte) 0x18 }; // e
		case 'f':
			return new byte[] { (byte) 0x08, (byte) 0x7E, (byte) 0x09, (byte) 0x01 }; // f
		case 'g':
			return new byte[] { (byte) 0x98, (byte) 0xA4, (byte) 0xA4, (byte) 0xA4, (byte) 0x58 }; // g
		case 'h':
			return new byte[] { (byte) 0x7F, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x78 }; // h
		case 'i':
			return new byte[] { (byte) 0x7D }; // i
		case 'j':
			return new byte[] { (byte) 0x80, (byte) 0x80, (byte) 0x7D }; // j
		case 'k':
			return new byte[] { (byte) 0x7F, (byte) 0x10, (byte) 0x10, (byte) 0x28, (byte) 0x44 }; // k
		case 'l':
			return new byte[] { (byte) 0x3F, (byte) 0x40, (byte) 0x40 }; // l
		case 'm':
			return new byte[] { (byte) 0x78, (byte) 0x04, (byte) 0x18, (byte) 0x04, (byte) 0x78 }; // m
		case 'n':
			return new byte[] { (byte) 0x7C, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x78 }; // n
		case 'o':
			return new byte[] { (byte) 0x38, (byte) 0x44, (byte) 0x44, (byte) 0x44, (byte) 0x38 }; // o
		case 'p':
			return new byte[] { (byte) 0xFC, (byte) 0x24, (byte) 0x24, (byte) 0x24, (byte) 0x18 }; // p
		case 'q':
			return new byte[] { (byte) 0x18, (byte) 0x24, (byte) 0x24, (byte) 0x24, (byte) 0xFC }; // q
		case 'r':
			return new byte[] { (byte) 0x7C, (byte) 0x08, (byte) 0x04, (byte) 0x04 }; // r
		case 's':
			return new byte[] { (byte) 0x48, (byte) 0x54, (byte) 0x54, (byte) 0x54, (byte) 0x24 }; // s
		case 't':
			return new byte[] { (byte) 0x04, (byte) 0x3F, (byte) 0x44, (byte) 0x40 }; // t
		case 'u':
			return new byte[] { (byte) 0x3C, (byte) 0x40, (byte) 0x40, (byte) 0x20, (byte) 0x7C }; // u
		case 'v':
			return new byte[] { (byte) 0x1C, (byte) 0x20, (byte) 0x40, (byte) 0x20, (byte) 0x1C }; // v
		case 'w':
			return new byte[] { (byte) 0x1C, (byte) 0x60, (byte) 0x18, (byte) 0x60, (byte) 0x1C }; // w
		case 'x':
			return new byte[] { (byte) 0x44, (byte) 0x28, (byte) 0x10, (byte) 0x28, (byte) 0x44 }; // x
		case 'y':
			return new byte[] { (byte) 0x9C, (byte) 0xA0, (byte) 0xA0, (byte) 0xA0, (byte) 0x7C }; // y
		case 'z':
			return new byte[] { (byte) 0x64, (byte) 0x54, (byte) 0x54, (byte) 0x54, (byte) 0x4C }; // z
		case '{':
			return new byte[] { (byte) 0x08, (byte) 0x36, (byte) 0x41 }; // {
		case '|':
			return new byte[] { (byte) 0x7F }; // |
		case '}':
			return new byte[] { (byte) 0x41, (byte) 0x36, (byte) 0x08 }; // }
		case '~':
			return new byte[] { (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01 }; // ~
		case '':
			return new byte[] { (byte) 0xFF, (byte) 0x81, (byte) 0x81, (byte) 0x81, (byte) 0xFF }; // 
		case 'Ą':
			return new byte[] { (byte) 0x7E, (byte) 0x11, (byte) 0x91, (byte) 0x51, (byte) 0x7E }; // Ą
		case '':
			return new byte[] { (byte) 0x3C, (byte) 0x42, (byte) 0x46, (byte) 0x43, (byte) 0x24 }; // 
		case '‚':
			return new byte[] { (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0xC1, (byte) 0x81 }; // ‚
		case '':
			return new byte[] { (byte) 0x7F, (byte) 0x48, (byte) 0x44, (byte) 0x42, (byte) 0x40 }; // 
		case '„':
			return new byte[] { (byte) 0x7E, (byte) 0x04, (byte) 0x0A, (byte) 0x11, (byte) 0x7E }; // „
		case '…':
			return new byte[] { (byte) 0x3C, (byte) 0x42, (byte) 0x46, (byte) 0x43, (byte) 0x3C }; // …
		case '†':
			return new byte[] { (byte) 0x24, (byte) 0x4A, (byte) 0x4E, (byte) 0x4B, (byte) 0x32 }; // †
		case '‡':
			return new byte[] { (byte) 0x62, (byte) 0x56, (byte) 0x4B, (byte) 0x4A, (byte) 0x46 }; // ‡
		case '':
			return new byte[] { (byte) 0x69, (byte) 0x59, (byte) 0x49, (byte) 0x4D, (byte) 0x4B }; // 
		case 'ą':
			return new byte[] { (byte) 0x20, (byte) 0x54, (byte) 0x54, (byte) 0xF8, (byte) 0x80 }; // ą
		case 'Š':
			return new byte[] { (byte) 0x38, (byte) 0x44, (byte) 0x46, (byte) 0x45 }; // Š
		case '‹':
			return new byte[] { (byte) 0x38, (byte) 0x54, (byte) 0x54, (byte) 0xD4, (byte) 0x98 }; // ‹
		case 'Ś':
			return new byte[] { (byte) 0x08, (byte) 0x3F, (byte) 0x42, (byte) 0x40 }; // Ś
		case 'Ť':
			return new byte[] { (byte) 0x7C, (byte) 0x04, (byte) 0x06, (byte) 0x05, (byte) 0x78 }; // Ť
		case 'Ž':
			return new byte[] { (byte) 0x38, (byte) 0x44, (byte) 0x46, (byte) 0x45, (byte) 0x38 }; // Ž
		case 'Ź':
			return new byte[] { (byte) 0x48, (byte) 0x54, (byte) 0x56, (byte) 0x55, (byte) 0x24 }; // Ź
		case '':
			return new byte[] { (byte) 0x64, (byte) 0x54, (byte) 0x56, (byte) 0x55, (byte) 0x4C }; // 
		case '‘':
			return new byte[] { (byte) 0x64, (byte) 0x54, (byte) 0x55, (byte) 0x54, (byte) 0x4C }; // ‘
		default:
			return null;
		}
	}

}
