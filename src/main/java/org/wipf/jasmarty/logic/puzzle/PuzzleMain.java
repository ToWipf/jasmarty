package org.wipf.jasmarty.logic.puzzle;

import java.io.File;
import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
