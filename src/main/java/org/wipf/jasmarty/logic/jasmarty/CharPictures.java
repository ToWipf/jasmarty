package org.wipf.jasmarty.logic.jasmarty;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.CustomChar;

@ApplicationScoped
public class CharPictures {

	private static final boolean T = true;
	private static final boolean F = false;

	public void wipf() {
		CustomChar c1 = new CustomChar();
		CustomChar c2 = new CustomChar();
		CustomChar c3 = new CustomChar();
		CustomChar c4 = new CustomChar();
		CustomChar c5 = new CustomChar();
		CustomChar c6 = new CustomChar();

		c1.setLine(F, T, T, T, F, 0);
		c1.setLine(T, T, T, T, F, 1);
		c1.setLine(F, F, T, T, F, 2);
		c1.setLine(F, T, F, T, T, 3);
		c1.setLine(T, F, F, T, T, 4);
		c1.setLine(F, T, T, T, T, 5);
		c1.setLine(T, F, F, T, T, 6);
		c1.setLine(T, F, F, T, T, 7);

		c2.setLine(F, F, F, T, T, 0);
		c2.setLine(F, F, T, T, T, 1);
		c2.setLine(F, F, T, T, T, 2);
		c2.setLine(F, F, T, T, T, 3);
		c2.setLine(T, T, F, F, T, 4);
		c2.setLine(T, T, T, T, T, 5);
		c2.setLine(T, F, F, F, T, 6);
		c2.setLine(T, T, F, F, T, 7);

		c3.setLine(T, T, F, F, F, 0);
		c3.setLine(T, T, T, F, F, 1);
		c3.setLine(T, T, F, F, F, 2);
		c3.setLine(T, T, F, F, F, 3);
		c3.setLine(T, F, F, F, F, 4);
		c3.setLine(T, T, F, F, F, 5);
		c3.setLine(T, T, T, F, F, 6);
		c3.setLine(T, F, T, F, F, 7);

		c4.setLine(T, F, F, F, T, 0);
		c4.setLine(F, T, T, T, T, 1);
		c4.setLine(F, T, T, T, T, 2);
		c4.setLine(T, F, T, F, T, 3);
		c4.setLine(T, T, T, F, T, 4);
		c4.setLine(T, T, T, F, T, 5);
		c4.setLine(T, T, F, F, T, 6);
		c4.setLine(F, T, T, T, F, 7);

		c5.setLine(T, F, F, F, T, 0);
		c5.setLine(T, T, T, T, T, 1);
		c5.setLine(T, F, T, T, T, 2);
		c5.setLine(T, F, T, T, T, 3);
		c5.setLine(T, T, T, T, F, 4);
		c5.setLine(T, T, T, T, T, 5);
		c5.setLine(T, T, T, T, T, 6);
		c5.setLine(F, F, T, T, T, 7);

		c6.setLine(T, F, T, T, F, 0);
		c6.setLine(T, F, T, T, F, 1);
		c6.setLine(F, F, T, T, F, 2);
		c6.setLine(F, F, T, F, F, 3);
		c6.setLine(F, T, T, F, T, 4);
		c6.setLine(F, T, T, T, T, 5);
		c6.setLine(T, T, T, F, T, 6);
		c6.setLine(T, T, T, T, F, 7);

	}

}
