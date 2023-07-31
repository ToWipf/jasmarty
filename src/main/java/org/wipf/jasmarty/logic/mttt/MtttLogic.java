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
	 * 
	 * true wenn entschieden
	 * 
	 * @param feldId
	 * @return TODO brauchs nicht
	 */
	private boolean pruefeUnterfeld(Integer feldId) {
		mtttPunkt np = getUnterfeldNullPunkt(feldId);
		mtttData[][] tttFeld = new mtttData[3][3];

		tttFeld[0][0] = this.cache.getByXY(0 + np.x, 0 + np.y);
		tttFeld[0][1] = this.cache.getByXY(0 + np.x, 1 + np.y);
		tttFeld[0][2] = this.cache.getByXY(0 + np.x, 2 + np.y);
		tttFeld[1][0] = this.cache.getByXY(1 + np.x, 0 + np.y);
		tttFeld[1][1] = this.cache.getByXY(1 + np.x, 1 + np.y);
		tttFeld[1][2] = this.cache.getByXY(1 + np.x, 2 + np.y);
		tttFeld[2][0] = this.cache.getByXY(2 + np.x, 0 + np.y);
		tttFeld[2][1] = this.cache.getByXY(2 + np.x, 1 + np.y);
		tttFeld[2][2] = this.cache.getByXY(2 + np.x, 2 + np.y);

		switch (doFeldAuswertung(tttFeld)) {
		case "X":
			mtttData mx = new mtttData();
			mx.setFarbe(farbe.ROT);
			mx.funktion = "GX";
			sezteUnterfeldAuswertungsFarbe(feldId, mx);
			return true;
		case "Y":
			mtttData my = new mtttData();
			my.setFarbe(farbe.GRUEN);
			my.funktion = "GY";
			sezteUnterfeldAuswertungsFarbe(feldId, my);
			return true;
		case "U":
			mtttData mu = new mtttData();
			mu.setFarbe(farbe.BLAU);
			mu.funktion = "U";
			sezteUnterfeldAuswertungsFarbe(feldId, mu);
			return true;
		default:
			return false;
		}
	}

	private void sezteUnterfeldAuswertungsFarbe(int feldId, mtttData m) {
		mtttPunkt np = getUnterfeldNullPunkt(feldId);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				this.cache.setByXY(x + np.x, y + np.y, m);
			}
		}
	}

	/**
	 * true wenn X und X oder Y und Y
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	private boolean vergleicheUnterspielFeld(mtttData A, mtttData B) {
		if (!A.funktion.equals("F") && !A.funktion.equals("M")) {
			return (A.funktion.equals(B.funktion));
		}
		return false;
	}

	/**
	 * Feldauswertung einen [][], unterspiel oder gesamt
	 * 
	 * @param tttFeld
	 * @return X, Y, U, ''
	 */
	private String doFeldAuswertung(mtttData[][] tttFeld) {

		// Gewonnen?
		for (int x = 0; x < 3; x++) {
			if (vergleicheUnterspielFeld(tttFeld[x][0], tttFeld[x][1]) && vergleicheUnterspielFeld(tttFeld[x][0], (tttFeld[x][2]))) {
				return tttFeld[x][0].funktion;
			}
		}
		for (int y = 0; y < 3; y++) {
			if (vergleicheUnterspielFeld(tttFeld[0][y], tttFeld[1][y]) && vergleicheUnterspielFeld(tttFeld[0][y], tttFeld[2][y])) {
				return tttFeld[0][y].funktion;
			}
		}
		if (vergleicheUnterspielFeld(tttFeld[0][0], tttFeld[1][1]) && vergleicheUnterspielFeld(tttFeld[0][0], (tttFeld[2][2]))) {
			return tttFeld[0][0].funktion;
		}
		if (vergleicheUnterspielFeld(tttFeld[0][2], tttFeld[1][1]) && vergleicheUnterspielFeld(tttFeld[0][2], (tttFeld[2][0]))) {
			return tttFeld[0][2].funktion;
		}
		// Unentschieden testen (Zählen ob voll ist)
		int n = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (!tttFeld[x][y].funktion.equals("F")) {
					n++;
					if (n == 9) {
						return "U";
					}
				}
			}
		}
		// Spiel läuft noch
		return "";
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
	private Integer getNextesFeldID(Integer x, Integer y) {

		mtttPunkt nullpunkt = getUnterfeldNullPunkt(getFeldIDVonBitKoord(x, y));
		int feldIdX = x - nullpunkt.x;
		int feldIdY = y - nullpunkt.y;
		int nextFeldId = feldIdY + feldIdX * 3 + 1;

		// Zielfeld prüfen
		mtttPunkt npZiel = getUnterfeldNullPunkt(nextFeldId);

		// Prüfen ob Spielfeld noch Setzbar ist, dazu einfach das erste Feld holen und
		// "funktion" lesen
		String feldstatus = this.cache.getByXY(npZiel.x, npZiel.y).funktion;
		if (feldstatus.equals("U") || feldstatus.equals("GX") || feldstatus.equals("GY")) {
			return 0; // 0 = ALLE
		}

		return nextFeldId;
	}

	/**
	 * Alle Markierungen löschen
	 */
	private void deMarkiereAlles() {
		for (mtttData[] x : this.cache.getCache()) {
			for (mtttData y : x) {
				if (y.funktion.startsWith("M")) {
					y.setFarbe(farbe.SCHWARZ);
					y.funktion = "F"; // Frei
				}
			}
		}
	}

	/**
	 * Markiern und endmarkieren
	 * 
	 * @param feld
	 */
	private void markiereFeld(Integer feldId) {

		if (feldId == 0) {
			// Freie auswahl - Alles Markieren
			for (int i = 0; i < 9; i++) {
				markiereUnterfeldIDGelb(i + 1);
			}
		} else {
			markiereUnterfeldIDGelb(feldId);
		}

	}

	private void markiereUnterfeldIDGelb(int feldId) {
		mtttPunkt nP = getUnterfeldNullPunkt(feldId);
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
	 * True wenn alles ausgespielt ist
	 * 
	 * @return
	 */
	private void pruefeUnterfelderGewinne() {

		for (int i = 0; i < 9; i++) {
			pruefeUnterfeld(i + 1);
		}

	}

	/**
	 * prüfen und LED im Eck schalten
	 */
	private boolean pruefeGesamtsieg() {
		mtttData[][] tttFull = new mtttData[3][3];

		tttFull[0][0] = this.cache.getByXY(1, 1);
		tttFull[0][1] = this.cache.getByXY(1, 5);
		tttFull[0][2] = this.cache.getByXY(1, 9);
		tttFull[1][0] = this.cache.getByXY(5, 1);
		tttFull[1][1] = this.cache.getByXY(5, 5);
		tttFull[1][2] = this.cache.getByXY(5, 9);
		tttFull[2][0] = this.cache.getByXY(9, 1);
		tttFull[2][1] = this.cache.getByXY(9, 5);
		tttFull[2][2] = this.cache.getByXY(9, 9);

		mtttData mStatusPixel = new mtttData();
		switch (doFeldAuswertung(tttFull)) {
		case "GX":
			mStatusPixel.setFarbe(farbe.ROT);
			mStatusPixel.funktion = "GGX";
			break;
		case "GY":
			mStatusPixel.setFarbe(farbe.GRUEN);
			mStatusPixel.funktion = "GGY";
			break;
		case "U":
			mStatusPixel.setFarbe(farbe.BLAU);
			mStatusPixel.funktion = "GGU";
			break;
		default:
			mStatusPixel.setFarbe(farbe.SCHWARZ);
			mStatusPixel.funktion = "W"; // Weiter
			break;
		}
		this.cache.setByXY(14, 14, mStatusPixel);
		return mStatusPixel.funktion.equals("W");
	}

	/**
	 * 
	 */
	public void setzeFeldUndWechselSpieler(Integer x, Integer y) {
		mtttData werd = new mtttData();
		mtttData feld = cache.getByXY(x, y);
		spieler.letztesFeld = feld.funktion; // F Nummer speichern
		deMarkiereAlles();

		// Nächstes Feld festlegen
		// TODO convert 3x3 Kord zu F NR

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

		pruefeUnterfelderGewinne();
		if (pruefeGesamtsieg()) {
			// Mögliche Felder für Spieler markieren
			markiereFeld(getNextesFeldID(x, y));
		} else {
			// Spiel Vorbei
		}

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
