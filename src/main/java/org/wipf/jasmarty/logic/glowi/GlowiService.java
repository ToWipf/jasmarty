package org.wipf.jasmarty.logic.glowi;

import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class GlowiService {

	public static final Integer SIZE = 15;

	private static final Logger LOGGER = Logger.getLogger("Glowi");

	public enum modus_type {
		MTTT, RND, FLIP
	};

	public modus_type modus;

	@Inject
	GlowiCache cache;
	@Inject
	Wipf wipf;
	@Inject
	GA_Mttt mttt;
	@Inject
	GA_RND rnd;
	@Inject
	GA_Flip flip;

	/**
	 * @return
	 */
	public String getFullScreen() {
		StringBuilder sb = new StringBuilder();

		boolean inverntLine = true;
		for (int x = 0; x < GlowiService.SIZE; x++) {
			inverntLine = !inverntLine;
			for (int y = 0; y < GlowiService.SIZE; y++) {
				int koordinatenIndex = (y + x * GlowiService.SIZE);
				if (inverntLine) {
					koordinatenIndex = koordinatenIndex + GlowiService.SIZE - y - y - 1;
				}

				GlowiData val = cache.getByXY(x, y);

				sb.append(String.format("%03d", koordinatenIndex)); // Kein unsigned byte oder char in Java :(
				if (val.farbe_R < 127) {
					sb.append((char) val.farbe_R);
				} else {
					LOGGER.warn("R zu hoch! " + val.farbe_R);
					sb.append((char) 127);
				}
				if (val.farbe_G < 127) {
					sb.append((char) val.farbe_G);
				} else {
					LOGGER.warn("G zu hoch! " + val.farbe_G);
					sb.append((char) 127);
				}
				if (val.farbe_B < 127) {
					sb.append((char) val.farbe_B);
				} else {
					LOGGER.warn("B zu hoch! " + val.farbe_B);
					sb.append((char) 127);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * @return
	 */
	public String getDivScreen() {
		StringBuilder sb = new StringBuilder();

		for (Entry<Integer, GlowiData> ch : cache.getChanges().entrySet()) {
			sb.append(String.format("%03d", ch.getKey())); // Kein unsigned byte oder char in Java :(
			sb.append((char) ch.getValue().farbe_R);
			sb.append((char) ch.getValue().farbe_G);
			sb.append((char) ch.getValue().farbe_B);
		}

		return sb.toString();
	}

	/**
	 * 
	 */
	public void doSet(Integer x, Integer y) {
		if (x < SIZE && y < SIZE) {

			if (modus == modus_type.MTTT) {
				mttt.doSet(x, y);
			} else if (modus == modus_type.FLIP) {
				flip.doSet(x, y);
			} else {
				rnd.doRNDInput(x, y);
			}
		} else {
			LOGGER.warn("doSet zu groß: " + x + "/" + y);
		}
	}

	/**
	 * @param id
	 */
	public void doSetById(Integer id) {
		if (id < SIZE * SIZE) {

			int x = 0;
			int y = 0;

			x = id / SIZE;
			y = id % SIZE;

			// invert y bei jeder 2. reihe
			if (x % 2 == 1) {
				y = GlowiService.SIZE - y - 1;
			}

			doSet(x, y);
		} else {
			LOGGER.warn("doSetById zu groß: " + id);
		}
	}

	/**
	 * 
	 */
	public void cls() {
		modus = modus_type.RND;
		cache.cls();
	}

	/**
	 * @return
	 */
	public String startApp() {
		// Spiele durchschalten
		if (modus == modus_type.RND) {
			mttt.loadNewGame();
			modus = modus_type.MTTT;
		} else if (modus == modus_type.MTTT) {
			flip.loadNewGame();
			modus = modus_type.FLIP;
		} else {
			rnd.loadNewGame();
			modus = modus_type.RND;
		}
		return getDivScreen();
	}

	/**
	 * @param gd
	 */
	public void setFullScreen(GlowiData[][] gd) {
		cache.setFull(gd);
	}

}
