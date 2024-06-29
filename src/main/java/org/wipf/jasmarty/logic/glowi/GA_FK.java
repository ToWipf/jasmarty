package org.wipf.jasmarty.logic.glowi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.glowi.GlowiData.farbe;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * Spiel: FarbenKampf
 * 
 * Gehe mit meiner Farbe zum Pixel
 */
@RequestScoped
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
		doChaos();
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doSet(int xIn, int yIn) {
		doChaos();
	}

	private void doChaos() {
		for (int x = 0; x < gservice.getSize(); x++) {
			for (int y = 0; y < gservice.getSize(); y++) {
				this.cache.getByXY(x, y).setFarbe(getRNDFarbe());
			}
		}
	}

	private farbe getRNDFarbe() {
		switch (wipf.getRandomInt(7)) {
		case 0:
			return farbe.BLAU;
		case 1:
			return farbe.GELB;
		case 2:
			return farbe.GRUEN;
		case 3:
			return farbe.ROT;
		case 4:
			return farbe.SCHWARZ;
		case 5:
			return farbe.AUS;
		default:
			return farbe.GRAU;

		}
	}

}
