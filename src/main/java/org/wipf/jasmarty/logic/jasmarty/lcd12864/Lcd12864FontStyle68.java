package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Lcd12864FontStyle68 {

	/**
	 * 
	 */
	public int getFont1X = 6;
	/**
	 * 
	 */
	public int getFont1Y = 8;

	/**
	 * @param c
	 * @return
	 */
	public byte[] getChar(char c) {
		switch (c) {
		case ' ':
			return new byte[] { (byte) 0x00 }; //
		case '!':
			return new byte[] { (byte) 0x5F, (byte) 0x5F }; // !
		case '"':
			return new byte[] { (byte) 0x07, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x07 }; // "
		case '#':
			return new byte[] { (byte) 0x14, (byte) 0x7F, (byte) 0x7F, (byte) 0x14, (byte) 0x7F, (byte) 0x7F,
					(byte) 0x14 }; // #
		case '$':
			return new byte[] { (byte) 0x24, (byte) 0x2E, (byte) 0x6B, (byte) 0x6B, (byte) 0x3A, (byte) 0x12 }; // $
		case '%':
			return new byte[] { (byte) 0x63, (byte) 0x73, (byte) 0x18, (byte) 0x0C, (byte) 0x67, (byte) 0x63 }; // %
		case '&':
			return new byte[] { (byte) 0x32, (byte) 0x7F, (byte) 0x4D, (byte) 0x4D, (byte) 0x77, (byte) 0x72,
					(byte) 0x50 }; // &
		case '\'':
			return new byte[] { (byte) 0x06, (byte) 0x09, (byte) 0x09, (byte) 0x06, (byte) 0x00, (byte) 0x00 }; // '
		case '(':
			return new byte[] { (byte) 0x1C, (byte) 0x3E, (byte) 0x63, (byte) 0x41, (byte) 0x00, (byte) 0x00,
					(byte) 0x00 }; // (
		case ')':
			return new byte[] { (byte) 0x41, (byte) 0x63, (byte) 0x3E, (byte) 0x1C }; // )
		case '*':
			return new byte[] { (byte) 0x08, (byte) 0x2A, (byte) 0x3E, (byte) 0x1C, (byte) 0x3E, (byte) 0x2A,
					(byte) 0x08 }; // *
		case '+':
			return new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x3E, (byte) 0x3E, (byte) 0x08, (byte) 0x08 }; // +
		case ',':
			return new byte[] { (byte) 0x80, (byte) 0xE0, (byte) 0x60 }; // , (byte)
		case '-':
			return new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x08 }; // -
		case '.':
			return new byte[] { (byte) 0x60, (byte) 0x60 }; // .
		case '/':
			return new byte[] { (byte) 0x40, (byte) 0x60, (byte) 0x30, (byte) 0x18, (byte) 0x0C, (byte) 0x06,
					(byte) 0x02 }; // /
		case '0':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x49, (byte) 0x45, (byte) 0x7F, (byte) 0x3E }; // 0
		case '1':
			return new byte[] { (byte) 0x40, (byte) 0x44, (byte) 0x7F, (byte) 0x7F, (byte) 0x40, (byte) 0x40 }; // 1
		case '2':
			return new byte[] { (byte) 0x62, (byte) 0x73, (byte) 0x59, (byte) 0x49, (byte) 0x4F, (byte) 0x46 }; // 2
		case '3':
			return new byte[] { (byte) 0x22, (byte) 0x63, (byte) 0x49, (byte) 0x49, (byte) 0x7F, (byte) 0x36 }; // 3
		case '4':
			return new byte[] { (byte) 0x18, (byte) 0x1C, (byte) 0x16, (byte) 0x13, (byte) 0x7F, (byte) 0x7F,
					(byte) 0x10 }; // 4
		case '5':
			return new byte[] { (byte) 0x27, (byte) 0x67, (byte) 0x45, (byte) 0x45, (byte) 0x7D, (byte) 0x39 }; // 5
		case '6':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x7B, (byte) 0x32 }; // 6
		case '7':
			return new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x79, (byte) 0x7D, (byte) 0x07, (byte) 0x03 }; // 7
		case '8':
			return new byte[] { (byte) 0x36, (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x7F, (byte) 0x36 }; // 8
		case '9':
			return new byte[] { (byte) 0x26, (byte) 0x6F, (byte) 0x49, (byte) 0x49, (byte) 0x7F, (byte) 0x3E }; // 9
		case ':':
			return new byte[] { (byte) 0x24, (byte) 0x24 }; // :
		case ';':
			return new byte[] { (byte) 0x80, (byte) 0xC4, (byte) 0x44 }; // ;
		case '<':
			return new byte[] { (byte) 0x08, (byte) 0x1C, (byte) 0x36, (byte) 0x63, (byte) 0x41, (byte) 0x41 }; // <
		case '=':
			return new byte[] { (byte) 0x14, (byte) 0x14, (byte) 0x14, (byte) 0x14, (byte) 0x14, (byte) 0x14 }; // =
		case '>':
			return new byte[] { (byte) 0x41, (byte) 0x41, (byte) 0x63, (byte) 0x36, (byte) 0x1C, (byte) 0x08 }; // >
		case '?':
			return new byte[] { (byte) 0x02, (byte) 0x03, (byte) 0x51, (byte) 0x59, (byte) 0x0F, (byte) 0x06 }; // ?
		case '@':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x41, (byte) 0x4D, (byte) 0x6F, (byte) 0x2E }; // @
		case 'A':
			return new byte[] { (byte) 0x7C, (byte) 0x7E, (byte) 0x13, (byte) 0x13, (byte) 0x7E, (byte) 0x7C }; // A
		case 'B':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x7F, (byte) 0x36 }; // B
		case 'C':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x41, (byte) 0x41, (byte) 0x63, (byte) 0x22 }; // C
		case 'D':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x41, (byte) 0x41, (byte) 0x7F, (byte) 0x3E }; // D
		case 'E':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x49, (byte) 0x49, (byte) 0x41, (byte) 0x41 }; // E
		case 'F':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x09, (byte) 0x09, (byte) 0x01, (byte) 0x01 }; // F
		case 'G':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x41, (byte) 0x49, (byte) 0x7B, (byte) 0x3A }; // G
		case 'H':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x08, (byte) 0x08, (byte) 0x7F, (byte) 0x7F }; // H
		case 'I':
			return new byte[] { (byte) 0x41, (byte) 0x7F, (byte) 0x7F, (byte) 0x41 }; // I
		case 'J':
			return new byte[] { (byte) 0x20, (byte) 0x60, (byte) 0x41, (byte) 0x7F, (byte) 0x3F, (byte) 0x01 }; // J
		case 'K':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x1C, (byte) 0x36, (byte) 0x63, (byte) 0x41 }; // K
		case 'L':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40 }; // L
		case 'M':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x06, (byte) 0x0C, (byte) 0x06, (byte) 0x7F,
					(byte) 0x7F }; // M
		case 'N':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x0C, (byte) 0x18, (byte) 0x7F, (byte) 0x7F }; // N
		case 'O':
			return new byte[] { (byte) 0x3E, (byte) 0x7F, (byte) 0x41, (byte) 0x41, (byte) 0x7F, (byte) 0x3E }; // O
		case 'P':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x09, (byte) 0x09, (byte) 0x0F, (byte) 0x06 }; // P
		case 'Q':
			return new byte[] { (byte) 0x1E, (byte) 0x3F, (byte) 0x21, (byte) 0x61, (byte) 0x7F, (byte) 0x5E }; // Q
		case 'R':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x19, (byte) 0x39, (byte) 0x6F, (byte) 0x46 }; // R
		case 'S':
			return new byte[] { (byte) 0x26, (byte) 0x6F, (byte) 0x49, (byte) 0x49, (byte) 0x7B, (byte) 0x32 }; // S
		case 'T':
			return new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x7F, (byte) 0x7F, (byte) 0x01, (byte) 0x01 }; // T
		case 'U':
			return new byte[] { (byte) 0x3F, (byte) 0x7F, (byte) 0x40, (byte) 0x40, (byte) 0x7F, (byte) 0x3F }; // U
		case 'V':
			return new byte[] { (byte) 0x1F, (byte) 0x3F, (byte) 0x60, (byte) 0x60, (byte) 0x3F, (byte) 0x1F }; // V
		case 'W':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x30, (byte) 0x18, (byte) 0x30, (byte) 0x7F,
					(byte) 0x7F }; // W
		case 'X':
			return new byte[] { (byte) 0x41, (byte) 0x63, (byte) 0x3E, (byte) 0x3E, (byte) 0x63, (byte) 0x41 }; // X
		case 'Y':
			return new byte[] { (byte) 0x07, (byte) 0x0F, (byte) 0x78, (byte) 0x78, (byte) 0x0F, (byte) 0x07 }; // Y
		case 'Z':
			return new byte[] { (byte) 0x61, (byte) 0x71, (byte) 0x59, (byte) 0x4D, (byte) 0x47, (byte) 0x43 }; // Z
		case '[':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x41, (byte) 0x41 }; // [
		case '\\':
			return new byte[] { (byte) 0x02, (byte) 0x06, (byte) 0x0C, (byte) 0x18, (byte) 0x30, (byte) 0x60,
					(byte) 0x40 }; // BackSlash
		case ']':
			return new byte[] { (byte) 0x41, (byte) 0x41, (byte) 0x7F, (byte) 0x7F }; // ]
		case '^':
			return new byte[] { (byte) 0x08, (byte) 0x0C, (byte) 0xFE, (byte) 0xFE, (byte) 0x0C, (byte) 0x08 }; // ^
		case '_':
			return new byte[] { (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
					(byte) 0x80 }; // _
		case '`':
			return new byte[] { (byte) 0x1C, (byte) 0x3E, (byte) 0x63, (byte) 0x41, (byte) 0x63, (byte) 0x14,
					(byte) 0x14 }; // `
		case 'a':
			return new byte[] { (byte) 0x20, (byte) 0x74, (byte) 0x54, (byte) 0x54, (byte) 0x7C, (byte) 0x78 }; // a
		case 'b':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x44, (byte) 0x44, (byte) 0x7C, (byte) 0x38 }; // b
		case 'c':
			return new byte[] { (byte) 0x38, (byte) 0x7C, (byte) 0x44, (byte) 0x44, (byte) 0x44 }; // c
		case 'd':
			return new byte[] { (byte) 0x38, (byte) 0x7C, (byte) 0x44, (byte) 0x44, (byte) 0x7F, (byte) 0x7F }; // d
		case 'e':
			return new byte[] { (byte) 0x38, (byte) 0x7C, (byte) 0x54, (byte) 0x54, (byte) 0x5C, (byte) 0x18 }; // e
		case 'f':
			return new byte[] { (byte) 0x08, (byte) 0x7E, (byte) 0x7F, (byte) 0x09, (byte) 0x09 }; // f
		case 'g':
			return new byte[] { (byte) 0x98, (byte) 0xBC, (byte) 0xA4, (byte) 0xA4, (byte) 0xFC, (byte) 0x7C }; // g
		case 'h':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x04, (byte) 0x04, (byte) 0x7C, (byte) 0x78 }; // h
		case 'i':
			return new byte[] { (byte) 0x44, (byte) 0x7D, (byte) 0x7D, (byte) 0x40 }; // i
		case 'j':
			return new byte[] { (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xFD, (byte) 0x7D }; // j
		case 'k':
			return new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x10, (byte) 0x38, (byte) 0x6C, (byte) 0x44 }; // k
		case 'l':
			return new byte[] { (byte) 0x41, (byte) 0x7F, (byte) 0x7F, (byte) 0x40 }; // l
		case 'm':
			return new byte[] { (byte) 0x78, (byte) 0x7C, (byte) 0x0C, (byte) 0x38, (byte) 0x0C, (byte) 0x7C,
					(byte) 0x78 }; // m
		case 'n':
			return new byte[] { (byte) 0x7C, (byte) 0x7C, (byte) 0x04, (byte) 0x04, (byte) 0x7C, (byte) 0x78 }; // n
		case 'o':
			return new byte[] { (byte) 0x38, (byte) 0x7C, (byte) 0x44, (byte) 0x44, (byte) 0x7C, (byte) 0x38 }; // o
		case 'p':
			return new byte[] { (byte) 0xFC, (byte) 0xFC, (byte) 0x24, (byte) 0x24, (byte) 0x3C, (byte) 0x18 }; // p
		case 'q':
			return new byte[] { (byte) 0x18, (byte) 0x3C, (byte) 0x24, (byte) 0x24, (byte) 0xFC, (byte) 0xFC }; // q
		case 'r':
			return new byte[] { (byte) 0x7C, (byte) 0x7C, (byte) 0x04, (byte) 0x04, (byte) 0x0C, (byte) 0x08 }; // r
		case 's':
			return new byte[] { (byte) 0x48, (byte) 0x5C, (byte) 0x54, (byte) 0x54, (byte) 0x74, (byte) 0x24 }; // s
		case 't':
			return new byte[] { (byte) 0x04, (byte) 0x3F, (byte) 0x7F, (byte) 0x44, (byte) 0x44 }; // t
		case 'u':
			return new byte[] { (byte) 0x3C, (byte) 0x7C, (byte) 0x40, (byte) 0x40, (byte) 0x7C, (byte) 0x7C }; // u
		case 'v':
			return new byte[] { (byte) 0x1C, (byte) 0x3C, (byte) 0x60, (byte) 0x60, (byte) 0x3C, (byte) 0x1C }; // v
		case 'w':
			return new byte[] { (byte) 0x1C, (byte) 0x7C, (byte) 0x60, (byte) 0x38, (byte) 0x60, (byte) 0x7C,
					(byte) 0x1C }; // w
		case 'x':
			return new byte[] { (byte) 0x44, (byte) 0x6C, (byte) 0x38, (byte) 0x38, (byte) 0x6C, (byte) 0x44 }; // x
		case 'y':
			return new byte[] { (byte) 0x9C, (byte) 0xBC, (byte) 0xA0, (byte) 0xE0, (byte) 0x7C, (byte) 0x3C }; // y
		case 'z':
			return new byte[] { (byte) 0x44, (byte) 0x64, (byte) 0x74, (byte) 0x5C, (byte) 0x4C, (byte) 0x44 }; // z
		case '{':
			return new byte[] { (byte) 0x08, (byte) 0x3E, (byte) 0x7F, (byte) 0x41, (byte) 0x41 }; // {
		case '|':
			return new byte[] { (byte) 0xFF, (byte) 0xFF }; // |
		case '}':
			return new byte[] { (byte) 0x41, (byte) 0x41, (byte) 0x7F, (byte) 0x3E, (byte) 0x08 }; // }
		case '~':
			return new byte[] { (byte) 0x10, (byte) 0x18, (byte) 0x18, (byte) 0x18, (byte) 0x08 }; // ~
		default:
			return null;
		}
	}
}
