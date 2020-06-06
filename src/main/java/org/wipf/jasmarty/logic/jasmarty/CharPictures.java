package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
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
	private static final Logger LOGGER = Logger.getLogger("CharPictures");

	/**
	 * @throws SQLException
	 * 
	 */
	public void writeAndLoadWipf() {
		CustomChar cWipf[] = new CustomChar[6];

		cWipf[0] = new CustomChar();
		cWipf[1] = new CustomChar();
		cWipf[2] = new CustomChar();
		cWipf[3] = new CustomChar();
		cWipf[4] = new CustomChar();
		cWipf[5] = new CustomChar();

		cWipf[0].setPosition(2);
		cWipf[1].setPosition(3);
		cWipf[2].setPosition(4);
		cWipf[3].setPosition(5);
		cWipf[4].setPosition(6);
		cWipf[5].setPosition(7);

		cWipf[0].setName("wipf 1/6");
		cWipf[1].setName("wipf 2/6");
		cWipf[2].setName("wipf 3/6");
		cWipf[3].setName("wipf 4/6");
		cWipf[4].setName("wipf 5/6");
		cWipf[5].setName("wipf 6/6");

		cWipf[0].setId(1001);
		cWipf[1].setId(1002);
		cWipf[2].setId(1003);
		cWipf[3].setId(1004);
		cWipf[4].setId(1005);
		cWipf[5].setId(1006);

		cWipf[0].setLine(F, T, T, T, F, 0);
		cWipf[0].setLine(T, T, T, T, F, 1);
		cWipf[0].setLine(F, F, T, T, F, 2);
		cWipf[0].setLine(F, T, F, T, T, 3);
		cWipf[0].setLine(T, F, F, T, T, 4);
		cWipf[0].setLine(F, T, T, T, T, 5);
		cWipf[0].setLine(T, F, F, T, T, 6);
		cWipf[0].setLine(T, F, F, T, T, 7);

		cWipf[1].setLine(F, F, F, T, T, 0);
		cWipf[1].setLine(F, F, T, T, T, 1);
		cWipf[1].setLine(F, F, T, T, T, 2);
		cWipf[1].setLine(F, F, T, T, T, 3);
		cWipf[1].setLine(T, T, F, F, T, 4);
		cWipf[1].setLine(T, T, T, T, T, 5);
		cWipf[1].setLine(T, F, F, F, T, 6);
		cWipf[1].setLine(T, T, F, F, T, 7);

		cWipf[2].setLine(T, T, F, F, F, 0);
		cWipf[2].setLine(T, T, T, F, F, 1);
		cWipf[2].setLine(T, T, F, F, F, 2);
		cWipf[2].setLine(T, T, F, F, F, 3);
		cWipf[2].setLine(T, F, F, F, F, 4);
		cWipf[2].setLine(T, T, F, F, F, 5);
		cWipf[2].setLine(T, T, T, F, F, 6);
		cWipf[2].setLine(T, F, T, F, F, 7);

		cWipf[3].setLine(T, F, F, F, T, 0);
		cWipf[3].setLine(F, T, T, T, T, 1);
		cWipf[3].setLine(F, T, T, T, T, 2);
		cWipf[3].setLine(T, F, T, F, T, 3);
		cWipf[3].setLine(T, T, T, F, T, 4);
		cWipf[3].setLine(T, T, T, F, T, 5);
		cWipf[3].setLine(T, T, F, F, T, 6);
		cWipf[3].setLine(F, T, T, T, F, 7);

		cWipf[4].setLine(T, F, F, F, T, 0);
		cWipf[4].setLine(T, T, T, T, T, 1);
		cWipf[4].setLine(T, F, T, T, T, 2);
		cWipf[4].setLine(T, F, T, T, T, 3);
		cWipf[4].setLine(T, T, T, T, F, 4);
		cWipf[4].setLine(T, T, T, T, T, 5);
		cWipf[4].setLine(T, T, T, T, T, 6);
		cWipf[4].setLine(F, F, T, T, T, 7);

		cWipf[5].setLine(T, F, T, T, F, 0);
		cWipf[5].setLine(T, F, T, T, F, 1);
		cWipf[5].setLine(F, F, T, T, F, 2);
		cWipf[5].setLine(F, F, T, F, F, 3);
		cWipf[5].setLine(F, T, T, F, T, 4);
		cWipf[5].setLine(F, T, T, T, T, 5);
		cWipf[5].setLine(T, T, T, F, T, 6);
		cWipf[5].setLine(T, T, T, T, F, 7);

		try {
			customChars.saveToDB(cWipf[0]);
			customChars.saveToDB(cWipf[1]);
			customChars.saveToDB(cWipf[2]);
			customChars.saveToDB(cWipf[3]);
			customChars.saveToDB(cWipf[4]);
			customChars.saveToDB(cWipf[5]);
		} catch (Exception e) {
			LOGGER.warn("customChars.saveToDB" + e);
		}

		int[] ids = { 1001, 1002, 1003, 1004, 1005, 1006 };
		customChars.loadCharToLcdFromDB(ids);
	}

	/**
	 * um alle Chars zu Testen
	 */
	public void testChars() {
		CustomChar cTest[] = new CustomChar[8];

		cTest[0] = new CustomChar();
		cTest[1] = new CustomChar();
		cTest[2] = new CustomChar();
		cTest[3] = new CustomChar();
		cTest[4] = new CustomChar();
		cTest[5] = new CustomChar();
		cTest[6] = new CustomChar();
		cTest[7] = new CustomChar();

		cTest[0].setPosition(0);
		cTest[1].setPosition(1);
		cTest[2].setPosition(2);
		cTest[3].setPosition(3);
		cTest[4].setPosition(4);
		cTest[5].setPosition(5);
		cTest[6].setPosition(6);
		cTest[7].setPosition(7);

		cTest[0].setData("v0000001");
		cTest[1].setData("v0000011");
		cTest[2].setData("v0000111");
		cTest[3].setData("v0001111");
		cTest[4].setData("v0011111");
		cTest[5].setData("v0111111");
		cTest[6].setData("v1111111");
		cTest[7].setData("v1v1v1v1");

		customChars.loadCharToLcd(cTest);
	}

}
