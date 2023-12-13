package org.wipf.jasmarty.logic.glowi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.logic.base.Wipf;

@ApplicationScoped
public class GA_RND {

	@Inject
	GlowiCache cache;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("GA_RND");

	public void loadNewGame() {
		LOGGER.info("Neues Spiel");

		// Alles zurücksetzen
		this.cache.cls();
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doRNDInput(int x, int y) {
		GlowiData m = new GlowiData();
		m.farbe_R = wipf.getRandomInt(60);
		m.farbe_G = wipf.getRandomInt(60);
		m.farbe_B = wipf.getRandomInt(60);
		m.funktion = "C";
		this.cache.setByXY(x, y, m);
	}

}
