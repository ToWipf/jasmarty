package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.mttt.mtttData;

@ApplicationScoped
public class MtttLogic {

	@Inject
	MtttCache cache;

	public void loadNewGame() {
		mtttData m = new mtttData();
		m.farbe_R = 70;
		m.farbe_G = 70;
		m.farbe_B = 70;
		m.funktion = "B";
		this.cache.drawLineH(0, MtttService.SIZE - 1, 0, m);
		this.cache.drawLineH(0, MtttService.SIZE - 1, 4, m);
		this.cache.drawLineH(0, MtttService.SIZE - 1, 8, m);
		this.cache.drawLineH(0, MtttService.SIZE - 1, 12, m);

		this.cache.drawLineV(0, 0, MtttService.SIZE - 1, m);
		this.cache.drawLineV(4, 0, MtttService.SIZE - 1, m);
		this.cache.drawLineV(8, 0, MtttService.SIZE - 1, m);
		this.cache.drawLineV(12, 0, MtttService.SIZE - 1, m);
//		for (mtttData[] x : this.cache.getCache()) {
//			for (mtttData y : x) {
//				
//			}
//		}

	}

}
