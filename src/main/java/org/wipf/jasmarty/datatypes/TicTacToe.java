package org.wipf.jasmarty.datatypes;

import java.util.Random;

/**
 * @author wipf
 *
 */
public class TicTacToe extends Game {

	private Character[][] tttFeld = new Character[3][3];

	/**
	 * 
	 */
	public TicTacToe() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				tttFeld[x][y] = ' ';
			}
		}
	}

	/**
	 * @param sFeld xxxoooxxx x o x o o
	 * 
	 */
	public TicTacToe(String sFeld) {
		int n = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				tttFeld[x][y] = sFeld.charAt(n);
				n++;
			}
		}
	}

	/**
	 * @return
	 */
	public String tttToString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				sb.append(tttFeld[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	public String tttToNiceString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (tttFeld[x][y].equals('X')) {
					sb.append("🐧\t");
				}
				if (tttFeld[x][y].equals('O')) {
					sb.append("👻\t");
				}
				if (tttFeld[x][y].equals('F')) {
					sb.append(numberToSymbol(koordinateToNumber(x, y)) + "\t");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	public String getFieldString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				sb.append(tttFeld[x][y]);
			}
		}
		return sb.toString();
	}

	/**
	 * @param tttFeld
	 */
	public void setTttFeld(Character[][] tttFeld) {
		this.tttFeld = tttFeld;
	}

	/**
	 * @param x
	 * @param y
	 * @param s
	 */
	public Boolean setkoordinate(int x, int y, Character c) {
		if (this.tttFeld[x][y] != 'F') {
			return false;
		} else {
			this.tttFeld[x][y] = c;
			return true;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private Integer koordinateToNumber(Integer x, Integer y) {
		if (x == 0 && y == 0) {
			return 1;
		}
		if (x == 0 && y == 1) {
			return 2;
		}
		if (x == 0 && y == 2) {
			return 3;
		}
		if (x == 1 && y == 0) {
			return 4;
		}
		if (x == 1 && y == 1) {
			return 5;
		}
		if (x == 1 && y == 2) {
			return 6;
		}
		if (x == 2 && y == 0) {
			return 7;
		}
		if (x == 2 && y == 1) {
			return 8;
		}
		if (x == 2 && y == 2) {
			return 9;
		}
		return null;
	}

	/**
	 * @param n
	 * @param c
	 * @return
	 */
	public Boolean setByNummer(Integer n, Character c) {
		if (n == null) {
			return false;
		}
		switch (n) {
		case 1:
			return setkoordinate(0, 0, c);
		case 2:
			return setkoordinate(0, 1, c);
		case 3:
			return setkoordinate(0, 2, c);
		case 4:
			return setkoordinate(1, 0, c);
		case 5:
			return setkoordinate(1, 1, c);
		case 6:
			return setkoordinate(1, 2, c);
		case 7:
			return setkoordinate(2, 0, c);
		case 8:
			return setkoordinate(2, 1, c);
		case 9:
			return setkoordinate(2, 2, c);
		default:
			return false;
		}
	}

	/**
	 * @param check
	 * @param setChar
	 * @return
	 */
	private boolean logicDreiInEinerReihe(Character check, Character setChar) {
		// Reihen 1
		for (int x = 0; x < 3; x++) {
			if (tttFeld[x][0] == check && check == tttFeld[x][1]) {
				if (setkoordinate(x, 2, setChar)) {
					return true;
				}
			}
			if (tttFeld[x][1] == check && check == tttFeld[x][2]) {
				if (setkoordinate(x, 0, setChar)) {
					return true;
				}
			}
			if (tttFeld[x][0] == check && check == tttFeld[x][2]) {
				if (setkoordinate(x, 1, setChar)) {
					return true;
				}
			}
		}
		// Reihen 2
		for (int y = 0; y < 3; y++) {
			if (tttFeld[0][y] == check && check == tttFeld[1][y]) {
				if (setkoordinate(2, y, setChar)) {
					return true;
				}
			}
			if (tttFeld[1][y] == check && check == tttFeld[2][y]) {
				if (setkoordinate(0, y, setChar)) {
					return true;
				}
			}
			if (tttFeld[0][y] == check && check == tttFeld[2][y]) {
				if (setkoordinate(1, y, setChar)) {
					return true;
				}
			}
		}
		// Schräge 1
		if (tttFeld[0][0] == check && check == tttFeld[1][1]) {
			if (setkoordinate(2, 2, setChar)) {
				return true;
			}
		}
		if (tttFeld[1][1] == check && check == tttFeld[2][2]) {
			if (setkoordinate(0, 0, setChar)) {
				return true;
			}
		}
		if (tttFeld[2][2] == check && check == tttFeld[0][0]) {
			if (setkoordinate(1, 1, setChar)) {
				return true;
			}
		}
		// Schräge 2
		if (tttFeld[0][2] == check && check == tttFeld[2][0]) {
			if (setkoordinate(1, 1, setChar)) {
				return true;
			}
		}
		if (tttFeld[1][1] == check && check == tttFeld[2][0]) {
			if (setkoordinate(0, 2, setChar)) {
				return true;
			}
		}
		if (tttFeld[1][1] == check && check == tttFeld[0][2]) {
			if (setkoordinate(2, 0, setChar)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param c
	 * @return
	 */
	public Boolean cpuSetzen(Character c) {
		Random zufall = new Random();
		// Versuche zu Gewinnen
		if (logicDreiInEinerReihe('O', 'O')) {
			return true;
		}
		// Versuche zu verhindern
		if (logicDreiInEinerReihe('X', 'O')) {
			return true;
		}

		// Zufall nur in der mitte wage
		for (int n = 0; n < 35; n++) {
			if (setkoordinate(1, zufall.nextInt(3), c)) {
				return true;
			}
		}
		// Zufall nur in der mitte hoch
		for (int n = 0; n < 35; n++) {
			if (setkoordinate(zufall.nextInt(3), 1, c)) {
				return true;
			}
		}
		// Zufall
		for (int n = 0; n < 35; n++) {
			if (setkoordinate(zufall.nextInt(3), zufall.nextInt(3), c)) {
				return true;
			}
		}
		// Zwangssetzen
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (tttFeld[x][y] == 'F') {
					return setkoordinate(x, y, c);
				}
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public Character auswertung() {
		// Gewonnen?
		for (int x = 0; x < 3; x++) {
			if (tttFeld[x][0] == tttFeld[x][1] && tttFeld[x][0] == tttFeld[x][2]) {
				return tttFeld[x][0];
			}
		}
		for (int y = 0; y < 3; y++) {
			if (tttFeld[0][y] == tttFeld[1][y] && tttFeld[0][y] == tttFeld[2][y]) {
				return tttFeld[0][y];
			}
		}
		if (tttFeld[0][0] == tttFeld[1][1] && tttFeld[0][0] == tttFeld[2][2]) {
			return tttFeld[0][0];
		}
		if (tttFeld[0][2] == tttFeld[1][1] && tttFeld[0][2] == tttFeld[2][0]) {
			return tttFeld[0][2];
		}
		// Unentschieden testen
		int n = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (tttFeld[x][y] != 'F') {
					n++;
				}
			}
		}
		if (n == 9) {
			return 'U';
		}
		// Spiel läuft noch
		return null;
	}

	/**
	 * TODO
	 * 
	 * @param n
	 * @return Number als Symbol
	 */
	private String numberToSymbol(Integer n) {
		// 1⃣
		if (n <= 9) {
			return n.toString() + "%E2%83%A3";
		}
		return null;
	}

}
