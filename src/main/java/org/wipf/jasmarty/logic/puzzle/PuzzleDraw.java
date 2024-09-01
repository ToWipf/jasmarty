package org.wipf.jasmarty.logic.puzzle;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import org.wipf.jasmarty.datatypes.puzzle.PuzzleFeld;
import org.wipf.jasmarty.datatypes.puzzle.PuzzleTeil;
import org.wipf.jasmarty.datatypes.puzzle.PuzzleTeil.verbindungsart;

import jakarta.enterprise.context.RequestScoped;

/**
 * @author devbuntu
 *
 */
@RequestScoped
public class PuzzleDraw {

	/**
	 * @return
	 * @throws IOException
	 */
	public File drawTest() throws IOException {
		PuzzleFeld pf = new PuzzleFeld();

		PuzzleTeil testteil = new PuzzleTeil(verbindungsart.BUCHSE, verbindungsart.BUCHSE, verbindungsart.STIFT, verbindungsart.STIFT);

		pf.setSize(2, 2);
		pf.addTeil(0, 0, testteil);
		pf.addTeil(0, 1, testteil);
		pf.addTeil(1, 0, testteil);
		pf.addTeil(1, 1, testteil);

		return feldToSvg(pf);

		// return drawTest_old();
	}

	/**
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public File feldToSvg(PuzzleFeld fd) throws IOException {
		SVGGraphics2D svg = new SVGGraphics2D(100, 100);

		int x = 0;
		int scale = 10;
		int offset = 10;

		for (PuzzleTeil[] pa : fd.getTeile()) {
			int y = 0;
			for (PuzzleTeil p : pa) {
				System.out.println("Teil " + x + " " + y + "TODO: " + p);
				// Ein Teil zeichnen
				svg = doDrawVert(svg, (x * scale) + offset, (y * scale) + offset, scale);
				svg = doDrawHor(svg, (x * scale) + offset, (y * scale) + offset, scale);
				svg = doDrawVert(svg, ((x * scale) + scale) + offset, ((y * scale) + scale) + offset, -scale);
				svg = doDrawHor(svg, ((x * scale) + scale) + offset, ((y * scale) + scale) + offset, -scale);
				y++;
			}
			x++;
		}
		File f = new File("files/puzzle.svg");

		SVGUtils.writeToSVG(f, svg.getSVGElement());
		return f;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public File drawTest_old() throws IOException {
		Integer height_y = 4;
		Integer weight_x = 3;
		Integer scale = 10;

		SVGGraphics2D g2 = new SVGGraphics2D(weight_x * scale, height_y * scale);

		for (int x = 0; x < weight_x; x++) {
			for (int y = 0; y < height_y; y++) {
				System.out.println("Quadrat X: " + x + " Y: " + y);

				g2 = doDrawVert(g2, x * scale, y * scale, scale);
				g2 = doDrawHor(g2, x * scale, y * scale, scale);

				g2 = doDrawVert(g2, (x * scale) + scale, (y * scale) + scale, -scale);
				g2 = doDrawHor(g2, (x * scale) + scale, (y * scale) + scale, -scale);
			}
		}
		Graphics aa = g2.create();
		aa.drawLine(10, 20, 30, 40);
		aa.drawLine(21, 20, 80, 60);

		// SVGGraphics2D x = new SVGGraphics2D(0, 0);

		// org.w3c.dom.Document.inp

		System.out.println("Save");
		File f = new File("files/puzzle.svg");

		SVGUtils.writeToSVG(f, g2.getSVGElement());
		return f;
	}

	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param len
	 * @return
	 */
	private SVGGraphics2D doDrawVert(SVGGraphics2D g, int x, int y, int len) {
		g.drawLine(x, y, len + x, y);
		return g;
	}

	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param len
	 * @return
	 */
	private SVGGraphics2D doDrawHor(SVGGraphics2D g, int x, int y, int len) {
		g.drawLine(x, y, x, len + y);
		return g;
	}

}
