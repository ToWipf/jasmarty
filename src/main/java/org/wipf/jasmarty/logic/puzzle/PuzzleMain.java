package org.wipf.jasmarty.logic.puzzle;

import java.io.File;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class PuzzleMain {

	@Inject
	PuzzleDraw draw;

	public File doTest() throws IOException {

		// PuzzleFeld f = new PuzzleFeld();

		return draw.drawTest();

		// f.addTeil(new PuzzleTeil(verbindungsart.BUCHSE, verbindungsart.BUCHSE,
		// verbindungsart.BUCHSE, verbindungsart.BUCHSE));

	}

}
