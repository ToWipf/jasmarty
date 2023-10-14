package org.wipf.jasmarty.logic.glowi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.datatypes.glowi.GlowiData.farbe;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * 
 */
@ApplicationScoped
public class GA_Flip {

	@Inject
	GlowiCache cache;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("GA_Flip");

	public void loadNewGame() {
		LOGGER.info("Neues Spiel");

		// Alles zurücksetzen
		this.cache.cls();

		// Zufallsfeld erstellen
		for (int px = 0; px < wipf.getRandomInt(50) + 10; px++) {
			GlowiData teil = new GlowiData();
			teil.funktion = "B";
			teil.setFarbe(getRNDFarbe());
			this.cache.setByXY(wipf.getRandomInt(15), wipf.getRandomInt(15), teil);
		}
	}

	/**
	 * @return
	 */
	private farbe getRNDFarbe() {
		switch (wipf.getRandomInt(5)) {
		case 0:
			return farbe.BLAU;
		case 1:
			return farbe.GELB;
		case 2:
			return farbe.GRUEN;
		case 3:
			return farbe.ROT;
		default:
			return farbe.GRAU;

		}
	}

	/**
	 * @param t
	 * @param clickFarbe
	 */
	private GlowiData filpTeil(GlowiData t, int farbeR, int farbeG, int farbeB) {
		if (t.funktion == "N") {
			t.funktion = "B";
			t.farbe_R = farbeR;
			t.farbe_G = farbeG;
			t.farbe_B = farbeB;
		} else {
			t.funktion = "N";
			t.setFarbe(farbe.AUS);
		}
		return t;
	}

	private boolean checkSieg() {
		// Referenz holen
		GlowiData referenz = this.cache.getByXY(0, 0);
		for (int x = 0; x < GlowiService.SIZE; x++) {
			for (int y = 0; y < GlowiService.SIZE; y++) {
				if (this.cache.getByXY(x, y).funktion != referenz.funktion) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 */
	private void SiegAllesBunt() {
		for (int x = 0; x < GlowiService.SIZE; x++) {
			for (int y = 0; y < GlowiService.SIZE; y++) {
				this.cache.getByXY(x, y).setFarbe(getRNDFarbe());
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doSet(Integer x, Integer y) {
		GlowiData tile = this.cache.getByXY(x, y);
		if (tile.funktion == "N") {
			// Teil hat noch keine Farbe
			tile.setFarbe(getRNDFarbe());
		}

		// die vier Nachbarn drehen
		if (x != 0) {
			this.cache.setByXY(x - 1, y, filpTeil(this.cache.getByXY(x - 1, y), tile.farbe_R, tile.farbe_G, tile.farbe_B));
		}
		if (x != 14) {
			this.cache.setByXY(x + 1, y, filpTeil(this.cache.getByXY(x + 1, y), tile.farbe_R, tile.farbe_G, tile.farbe_B));
		}
		if (y != 0) {
			this.cache.setByXY(x, y - 1, filpTeil(this.cache.getByXY(x, y - 1), tile.farbe_R, tile.farbe_G, tile.farbe_B));
		}
		if (y != 14) {
			this.cache.setByXY(x, y + 1, filpTeil(this.cache.getByXY(x, y + 1), tile.farbe_R, tile.farbe_G, tile.farbe_B));
		}
		this.cache.setByXY(x, y, filpTeil(tile, tile.farbe_R, tile.farbe_G, tile.farbe_B));

		if (checkSieg()) {
			SiegAllesBunt();
			SiegAllesBunt();
			SiegAllesBunt();
		}

	}

}
