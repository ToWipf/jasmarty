package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.datatypes.mttt.mtttData.farbe;
import org.wipf.jasmarty.datatypes.mttt.mtttSpieler;
import org.wipf.jasmarty.logic.mttt.MtttCache.modus_type;

@ApplicationScoped
public class MtttLogic {

	@Inject
	MtttCache cache;

	public mtttSpieler spieler;

	public void loadNewGame() {
		spieler = new mtttSpieler();
		cache.modus = modus_type.MTTT;

		// Alle zurücksetzen
		for (mtttData[] x : this.cache.getCache()) {
			for (mtttData y : x) {
				y.farbe_R = 0;
				y.farbe_G = 0;
				y.farbe_B = 0;
				y.funktion = "L";
			}
		}

		// Gitter erstellen
		mtttData randteil = new mtttData();
		randteil.setFarbe(farbe.GRAU);
		randteil.funktion = "B";
		this.cache.drawLineH(0, 11, 0, randteil);
		this.cache.drawLineH(0, 11, 4, randteil);
		this.cache.drawLineH(0, 11, 8, randteil);
		this.cache.drawLineH(0, 11, 12, randteil);

		this.cache.drawLineV(0, 0, 11, randteil);
		this.cache.drawLineV(4, 0, 11, randteil);
		this.cache.drawLineV(8, 0, 11, randteil);
		this.cache.drawLineV(12, 0, 12, randteil);

		// Pixel auserhalb setzen // TODO hier wird die SIZE nicht beachtet
		mtttData auserhalb = new mtttData();
		auserhalb.farbe_R = 0;
		auserhalb.farbe_G = 0;
		auserhalb.farbe_B = 0;
		auserhalb.funktion = "A";
		this.cache.drawLineH(0, 14, 13, auserhalb);
		this.cache.drawLineH(0, 14, 14, auserhalb);
		this.cache.drawLineV(13, 0, 14, auserhalb);
		this.cache.drawLineV(14, 0, 14, auserhalb);

		// Spielfelder
		mtttData feld = new mtttData();
		feld.funktion = "F1";
		this.cache.drawRectFill(1, 1, 3, 3, feld);
		feld.funktion = "F2";
		this.cache.drawRectFill(1, 5, 3, 3, feld);
		feld.funktion = "F3";
		this.cache.drawRectFill(1, 9, 3, 3, feld);
		feld.funktion = "F4";
		this.cache.drawRectFill(5, 1, 3, 3, feld);
		feld.funktion = "F5";
		this.cache.drawRectFill(5, 5, 3, 3, feld);
		feld.funktion = "F6";
		this.cache.drawRectFill(5, 9, 3, 3, feld);
		feld.funktion = "F7";
		this.cache.drawRectFill(9, 1, 3, 3, feld);
		feld.funktion = "F8";
		this.cache.drawRectFill(9, 5, 3, 3, feld);
		feld.funktion = "F9";
		this.cache.drawRectFill(9, 9, 3, 3, feld);

		// Initialer Spieler
		mtttData initSpieler = new mtttData();
		initSpieler.setFarbe(farbe.ROT);
		spieler.werIstDran = mtttSpieler.werdran.SPIELER_X;
		spieler.erlaubtesNaechstesFeld = "ALL";
		this.cache.setPixel(14, 0, initSpieler);
	}

	private String convert3x3toFNR(Integer x, Integer y) {
		int fXcord = 0;
		if (x >= 1 && x <= 3) {
			fXcord = 1;
		}
		if (x >= 4 && x <= 7) {
			fXcord = 2;
		}
		if (x >= 9 && x <= 11) {
			fXcord = 3;
		}

		int fYcord = 0;
		if (y >= 1 && y <= 3) {
			fXcord = 1;
		}
		if (y >= 4 && y <= 7) {
			fXcord = 2;
		}
		if (y >= 9 && y <= 11) {
			fXcord = 3;
		}

		int feldId = fXcord + fYcord * 3;

		return "F" + feldId;

		// return "ALL";
	}

	/**
	 * 
	 */
	public void setzeFeldUndWechselSpieler(Integer x, Integer y) {
		mtttData werd = new mtttData();
		mtttData feld = cache.getByXY(x, y);
		spieler.letztesFeld = feld.funktion; // F Nummer speichern

		// Nächstes Feld festlegen
		// TOOD convert 3x3 Kord zu F NR
		spieler.erlaubtesNaechstesFeld = convert3x3toFNR(x, y);

		if (spieler.werIstDran == mtttSpieler.werdran.SPIELER_X) {
			spieler.werIstDran = mtttSpieler.werdran.SPIELER_Y;
			feld.setFarbe(farbe.ROT);
			feld.funktion = "X";
			werd.setFarbe(farbe.GRUEN);
		} else {
			spieler.werIstDran = mtttSpieler.werdran.SPIELER_X;
			feld.setFarbe(farbe.GRUEN);
			feld.funktion = "Y";
			werd.setFarbe(farbe.ROT);
		}

		this.cache.setPixel(14, 0, werd);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void doSet(Integer x, Integer y) {
		mtttData m = cache.getByXY(x, y);

		if (spieler.erlaubtesNaechstesFeld.equals("ALL") || spieler.erlaubtesNaechstesFeld.equals(m.funktion)) {
			setzeFeldUndWechselSpieler(x, y);
		}

	}

}
