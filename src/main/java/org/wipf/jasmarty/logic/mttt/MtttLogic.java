package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.datatypes.mttt.mtttData.farbe;
import org.wipf.jasmarty.datatypes.mttt.mtttPunkt;
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
		feld.funktion = "M"; // Alle Felder Markieren
		feld.setFarbe(farbe.GELB);
		this.cache.drawRectFill(1, 1, 3, 3, feld);
		this.cache.drawRectFill(1, 5, 3, 3, feld);
		this.cache.drawRectFill(1, 9, 3, 3, feld);
		this.cache.drawRectFill(5, 1, 3, 3, feld);
		this.cache.drawRectFill(5, 5, 3, 3, feld);
		this.cache.drawRectFill(5, 9, 3, 3, feld);
		this.cache.drawRectFill(9, 1, 3, 3, feld);
		this.cache.drawRectFill(9, 5, 3, 3, feld);
		this.cache.drawRectFill(9, 9, 3, 3, feld);

		// Initialer Spieler
		mtttData initSpieler = new mtttData();
		initSpieler.setFarbe(farbe.ROT);
		spieler.werIstDran = mtttSpieler.werdran.SPIELER_X;
		spieler.erlaubtesNaechstesFeld = "ALL";
		this.cache.setPixel(14, 0, initSpieler);
	}

	/**
	 * Nullpunkt eines F Feldes von 1 bis 9 finden
	 * 
	 * @param feldID
	 * @return
	 */
	private mtttPunkt getUnterfeldNullPunkt(int feldID) {
		mtttPunkt mP = new mtttPunkt();
		switch (feldID) {
		case 1:
			mP.x = 1;
			mP.y = 1;
			break;
		case 2:
			mP.x = 1;
			mP.y = 5;
			break;
		case 3:
			mP.x = 1;
			mP.y = 9;
			break;
		case 4:
			mP.x = 5;
			mP.y = 1;
			break;
		case 5:
			mP.x = 5;
			mP.y = 5;
			break;
		case 6:
			mP.x = 5;
			mP.y = 9;
			break;
		case 7:
			mP.x = 9;
			mP.y = 1;
			break;
		case 8:
			mP.x = 9;
			mP.y = 5;
			break;
		case 9:
			mP.x = 9;
			mP.y = 9;
			break;
		default:
			return null;
		}
		return mP;
	}

	/**
	 * Rausfinden von welchen feld gekommen wird
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Integer getFeldIDVonBitKoord(Integer x, Integer y) {

		int bigKordX = -1;
		int bigKordY = -1;
		if (x >= 1 && x <= 3) {
			bigKordX = 0;
		}
		if (x >= 5 && x <= 7) {
			bigKordX = 1;
		}
		if (x >= 9 && x <= 11) {
			bigKordX = 2;
		}

		if (y >= 1 && y <= 3) {
			bigKordY = 0;
		}
		if (y >= 5 && y <= 7) {
			bigKordY = 1;
		}
		if (y >= 9 && y <= 11) {
			bigKordY = 2;
		}

		return (bigKordY + bigKordX * 3 + 1);

	}

	/**
	 * finde vom gesamtspiel ein unterspiel und davon die Koordinate als F Index
	 * zurückgeben
	 * 
	 * @param x
	 * @param y
	 * @param letztesFeld
	 * @return
	 */
	private String getNextesFeldID(Integer x, Integer y) {

		mtttPunkt nullpunkt = getUnterfeldNullPunkt(getFeldIDVonBitKoord(x, y));

		int feldIdX = x - nullpunkt.x;
		int feldIdY = y - nullpunkt.y;

		int nextFeldId = feldIdY + feldIdX * 3 + 1;

		// Prüfen ob Spielfeld noch Setzbar ist, dazu einfach das erste Feld holen und
		// "funktion" lesen
		String feldstatus = this.cache.getByXY(nullpunkt.x, nullpunkt.y).funktion;
		if (feldstatus.equals("U") || feldstatus.equals("GX") || feldstatus.equals("GY")) {
			return "ALL";
		}

		return "F" + nextFeldId;
	}

	/**
	 * Markiern und endmarkieren
	 * 
	 * @param feld
	 */
	private void markiereFeld(String feld) {
		int feldId = Integer.valueOf(feld.substring(1, 2));
		mtttPunkt nP = getUnterfeldNullPunkt(feldId);
		// Alte Markierungen löschen
		for (mtttData[] x : this.cache.getCache()) {
			for (mtttData y : x) {
				if (y.funktion.startsWith("M")) {
					y.setFarbe(farbe.SCHWARZ);
					y.funktion = "F"; // Frei
				}
			}
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				mtttData m = this.cache.getByXY(x + nP.x, y + nP.y);
				if (m.funktion.startsWith("F")) {
					m.setFarbe(farbe.GELB);
					m.funktion = "M"; // Markiert
				}
			}
		}

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
		spieler.erlaubtesNaechstesFeld = getNextesFeldID(x, y);
		// Mögliche Felder für Spieler markieren
		markiereFeld(spieler.erlaubtesNaechstesFeld);

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
		// if (spieler.erlaubtesNaechstesFeld.equals("ALL") ||
		// spieler.erlaubtesNaechstesFeld.equals(m.funktion)) {
		if (m.funktion.equals("M")) {
			setzeFeldUndWechselSpieler(x, y);
		}

	}

}
