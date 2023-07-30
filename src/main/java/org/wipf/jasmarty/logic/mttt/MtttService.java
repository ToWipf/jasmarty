package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.mttt.MtttCache.modus_type;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class MtttService {

	public static final Integer SIZE = 15;

	@Inject
	MtttCache cache;
	@Inject
	Wipf wipf;
	@Inject
	MtttLogic logic;

	/**
	 * @return
	 */
	public String getFullScreen() {
		return this.cache.toDataString();
	}

	/**
	 * 
	 */
	public void doSet(Integer x, Integer y) {
		if (cache.modus == modus_type.MTTT) {
			logic.doSet(x, y);
		} else {
			doInput(x, y);
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doInput(int x, int y) {
		System.out.println(x + " " + y);
		mtttData v = this.cache.getByXY(x, y);
		v.farbe_R = wipf.getRandomInt(60);
		v.farbe_G = wipf.getRandomInt(60);
		v.farbe_B = wipf.getRandomInt(60);
		v.funktion = "C";
	}

}
