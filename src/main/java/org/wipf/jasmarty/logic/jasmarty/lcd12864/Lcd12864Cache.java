package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864Cache {

	public static int SIZE = 1024;
	private byte[] baScreen = new byte[SIZE];

	/**
	 * @return
	 */
	public byte[] getBaScreen() {

		return baScreen;
	}

	/**
	 * @param baScreen
	 */
	public void setBaScreen(byte[] baScreen) {
		this.baScreen = baScreen;
	}

}
