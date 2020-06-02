package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.CustomChar;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class CharPictures {

	@Inject
	CustomChars customChars;

	private static final boolean T = true;
	private static final boolean F = false;

	/**
	 * @throws SQLException
	 * 
	 */
	public void writeAndLoadWipf() {
		CustomChar c1 = new CustomChar();
		CustomChar c2 = new CustomChar();
		CustomChar c3 = new CustomChar();
		CustomChar c4 = new CustomChar();
		CustomChar c5 = new CustomChar();
		CustomChar c6 = new CustomChar();

		c1.setPosition(1);
		c2.setPosition(2);
		c3.setPosition(3);
		c4.setPosition(4);
		c5.setPosition(5);
		c6.setPosition(6);

		/*
		 * c1.setPosition(0); c2.setPosition(1); c3.setPosition(2); c4.setPosition(3);
		 * c5.setPosition(4); c6.setPosition(5);
		 */

		c1.setName("wipf 1/6");
		c2.setName("wipf 2/6");
		c3.setName("wipf 3/6");
		c4.setName("wipf 4/6");
		c5.setName("wipf 5/6");
		c6.setName("wipf 6/6");

		c1.setId(1001);
		c2.setId(1002);
		c3.setId(1003);
		c4.setId(1004);
		c5.setId(1005);
		c6.setId(1006);

		c1.setLine(F, T, T, T, F, 0);
		c1.setLine(T, T, T, T, F, 1);
		c1.setLine(F, F, T, T, F, 2);
		c1.setLine(F, T, F, T, T, 3);
		c1.setLine(T, F, F, T, T, 4);
		c1.setLine(F, T, T, T, T, 5);
		c1.setLine(T, F, F, T, T, 6);
		c1.setLine(T, F, F, T, T, 7);

		c2.setLine(F, F, F, T, T, 0);
		c2.setLine(F, F, T, T, T, 1);
		c2.setLine(F, F, T, T, T, 2);
		c2.setLine(F, F, T, T, T, 3);
		c2.setLine(T, T, F, F, T, 4);
		c2.setLine(T, T, T, T, T, 5);
		c2.setLine(T, F, F, F, T, 6);
		c2.setLine(T, T, F, F, T, 7);

		c3.setLine(T, T, F, F, F, 0);
		c3.setLine(T, T, T, F, F, 1);
		c3.setLine(T, T, F, F, F, 2);
		c3.setLine(T, T, F, F, F, 3);
		c3.setLine(T, F, F, F, F, 4);
		c3.setLine(T, T, F, F, F, 5);
		c3.setLine(T, T, T, F, F, 6);
		c3.setLine(T, F, T, F, F, 7);

		c4.setLine(T, F, F, F, T, 0);
		c4.setLine(F, T, T, T, T, 1);
		c4.setLine(F, T, T, T, T, 2);
		c4.setLine(T, F, T, F, T, 3);
		c4.setLine(T, T, T, F, T, 4);
		c4.setLine(T, T, T, F, T, 5);
		c4.setLine(T, T, F, F, T, 6);
		c4.setLine(F, T, T, T, F, 7);

		c5.setLine(T, F, F, F, T, 0);
		c5.setLine(T, T, T, T, T, 1);
		c5.setLine(T, F, T, T, T, 2);
		c5.setLine(T, F, T, T, T, 3);
		c5.setLine(T, T, T, T, F, 4);
		c5.setLine(T, T, T, T, T, 5);
		c5.setLine(T, T, T, T, T, 6);
		c5.setLine(F, F, T, T, T, 7);

		c6.setLine(T, F, T, T, F, 0);
		c6.setLine(T, F, T, T, F, 1);
		c6.setLine(F, F, T, T, F, 2);
		c6.setLine(F, F, T, F, F, 3);
		c6.setLine(F, T, T, F, T, 4);
		c6.setLine(F, T, T, T, T, 5);
		c6.setLine(T, T, T, F, T, 6);
		c6.setLine(T, T, T, T, F, 7);

		try {
			customChars.saveToDB(c1);
			customChars.saveToDB(c2);
			customChars.saveToDB(c3);
			customChars.saveToDB(c4);
			customChars.saveToDB(c5);
			customChars.saveToDB(c6);
		} catch (Exception e) {
			System.out.println("Fail 1001");
		}

		customChars.loadCharToLcdFromDB(1001);
		customChars.loadCharToLcdFromDB(1002);
		customChars.loadCharToLcdFromDB(1003);
		customChars.loadCharToLcdFromDB(1004);
		customChars.loadCharToLcdFromDB(1005);
		customChars.loadCharToLcdFromDB(1006);
	}

	/**
	 * 
	 */
	public void testChars() {
		CustomChar c0 = new CustomChar();
		CustomChar c1 = new CustomChar();
		CustomChar c2 = new CustomChar();
		CustomChar c3 = new CustomChar();
		CustomChar c4 = new CustomChar();
		CustomChar c5 = new CustomChar();
		CustomChar c6 = new CustomChar();
		CustomChar c7 = new CustomChar();

		c0.setPosition(0);
		c1.setPosition(1);
		c2.setPosition(2);
		c3.setPosition(3);
		c4.setPosition(4);
		c5.setPosition(5);
		c6.setPosition(6);
		c7.setPosition(7);

		c0.setData("00000001");
		c1.setData("00000011");
		c2.setData("00000111");
		c3.setData("00001111");
		c4.setData("00011111");
		c5.setData("00111111");
		c6.setData("01111111");
		c7.setData("01010101");

		customChars.loadCharToLcd(c0);
		customChars.loadCharToLcd(c1);
		customChars.loadCharToLcd(c2);
		customChars.loadCharToLcd(c3);
		customChars.loadCharToLcd(c4);
		customChars.loadCharToLcd(c5);
		customChars.loadCharToLcd(c6);
		customChars.loadCharToLcd(c7);
	}

}
