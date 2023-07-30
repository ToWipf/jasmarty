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
	 * @return
	 */
	public String getFullScreen() {
		return this.mtttCache.toDataString();
	}

	/**
	 * 
	 */
	public void doSet(Integer x, Integer y) {
		doInput(x, y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doInput(int x, int y) {
		System.out.println(x + " " + y);
		mtttData v = this.mtttCache.getByXY(x, y);
		v.farbe_R = wipf.getRandomInt(60);
		v.farbe_G = wipf.getRandomInt(60);
		v.farbe_B = wipf.getRandomInt(60);
		v.funktion = "C";
	}

}
