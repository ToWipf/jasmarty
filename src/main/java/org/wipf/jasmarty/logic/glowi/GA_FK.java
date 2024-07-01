package org.wipf.jasmarty.logic.glowi;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.datatypes.glowi.GlowiData.farbe;
import org.wipf.jasmarty.logic.base.Wipf;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Spiel: FarbenKampf
 * 
 * Gehe mit meiner Farbe zum Pixel
 */
@ApplicationScoped
public class GA_FK {

	@Inject
	GlowiCache cache;
	@Inject
	Wipf wipf;
	@Inject
	GlowiService gservice;

	private static final Logger LOGGER = Logger.getLogger("GA_FK");

	public void loadNewGame() {
		LOGGER.info("Neues Spiel");

		// Alles zurücksetzen
		this.cache.cls();

		GlowiData teil = new GlowiData();
		teil.setFarbe(farbe.BLAU);
		teil.funktion = "B";

		this.cache.drawRectFill(0, 0, this.gservice.getSize(), 4, teil);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doSet(int x, int y) {

		String quellTeilname = this.cache.getByXY(x, y).funktion;
		GlowiData teil = new GlowiData();
		teil.setFarbe(farbe.ROT);
		teil.funktion = "S";

		this.cache.setByXY(x, y, teil);
	}

}
