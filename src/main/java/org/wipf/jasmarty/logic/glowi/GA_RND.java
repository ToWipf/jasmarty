package org.wipf.jasmarty.logic.glowi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.logic.base.Wipf;

@ApplicationScoped
public class GA_RND {

	@Inject
	GlowiCache cache;
	@Inject
	Wipf wipf;

	/**
	 * @param x
	 * @param y
	 */
	public void doRNDInput(int x, int y) {
		GlowiData v = this.cache.getByXY(x, y);
		v.farbe_R = wipf.getRandomInt(60);
		v.farbe_G = wipf.getRandomInt(60);
		v.farbe_B = wipf.getRandomInt(60);
		v.funktion = "C";
	}

}
