package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MtttService {

	public static final Integer SIZE = 15;

	@Inject
	MtttCache mtttCache;

	@Inject
	Wipf wipf;

	/**
	 * @param x
	 * @param y
	 */
	public void doInput(int x, int y) {
		mtttData v = this.mtttCache.getByXY(x, y);
		v.farbe_B = 10;
	}

	/**
	 * @return
	 */
	public String getFullScreen() {
		return this.mtttCache.toDataString();
	}

	/**
	 * @return
	 */
	public String getTestdata() {
		StringBuilder sb = new StringBuilder();

		System.out.println("IN");
		for (int i = 0; i < 10; i++) {
			char r = (char) wipf.getRandomInt(60);
			char g = (char) wipf.getRandomInt(10);
			char b = (char) wipf.getRandomInt(30);

			sb.append(String.format("%03d", i)); // Kein unsigned byte oder char in Java :(
			sb.append(r);
			sb.append(g);
			sb.append(b);

		}
		System.out.println("OUT");
		return sb.toString();
	}

}
