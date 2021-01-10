package org.wipf.jasmarty.datatypes.jasmarty;

public class Fonts {
	public byte[] getFont1(char c) {
		switch (c) {
		case ' ':
			return new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		case '!':
			return new byte[] { 0x5F, 0x5F, 0x00, 0x00, 0x00, 0x00, 0x00 };
		case '"':
			return new byte[] { 0x07, 0x07, 0x00, 0x00, 0x07, 0x07, 0x00 };
		case '#':
			return new byte[] { 0x14, 0x7F, 0x7F, 0x14, 0x7F, 0x7F, 0x14 };
		case '$':
			return new byte[] { 0x24, 0x2E, 0x6B, 0x6B, 0x3A, 0x12, 0x00 };
		case '%':
			return new byte[] { 0x63, 0x73, 0x18, 0x0C, 0x67, 0x63, 0x00 };
		// TODO ...
		case '2':
			return new byte[] { 0x62, 0x73, 0x59, 0x49, 0x4F, 0x46, 0x00 };
		// TODO ...
		default:
			return null;
		}
	}
}