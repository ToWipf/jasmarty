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

		StringBuilder sb = new StringBuilder();

		boolean inverntLine = true;
		for (int x = 0; x < MtttService.SIZE; x++) {
			inverntLine = !inverntLine;
			for (int y = 0; y < MtttService.SIZE; y++) {
				int koordinatenIndex = (y + x * MtttService.SIZE);
				if (inverntLine) {
					koordinatenIndex = koordinatenIndex + MtttService.SIZE + 1 - y - y - 2;
					System.out.println(koordinatenIndex);
				}

				mtttData val = cache.getByXY(x, y);

				sb.append(String.format("%03d", koordinatenIndex)); // Kein unsigned byte oder char in Java :(
				sb.append((char) val.farbe_R);
				sb.append((char) val.farbe_G);
				sb.append((char) val.farbe_B);
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 */
	public void doSet(Integer x, Integer y) {
		if (cache.modus == modus_type.MTTT) {
			logic.doSet(x, y);
		} else {
			doRNDInput(x, y);
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doRNDInput(int x, int y) {
		mtttData v = this.cache.getByXY(x, y);
		v.farbe_R = wipf.getRandomInt(60);
		v.farbe_G = wipf.getRandomInt(60);
		v.farbe_B = wipf.getRandomInt(60);
		v.funktion = "C";
	}

	/**
	 * 
	 */
	public void stopApp() {
		cache.modus = modus_type.NONE;
	}

	/**
	 * 
	 */
	public void cls() {
		cache.modus = modus_type.NONE;
		cache.cls();
	}

}
