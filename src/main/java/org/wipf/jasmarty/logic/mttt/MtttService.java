package org.wipf.jasmarty.logic.mttt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author devbuntu
 *
 */
@ApplicationScoped
public class MtttService {

	public static final Integer SIZE_X = 15;
	public static final Integer SIZE_Y = 15;

	@Inject
	Wipf wipf;

	public String getTestdata() {
		StringBuilder sb = new StringBuilder();

		System.out.println("IN");
		for (int i = 0; i < 10; i++) {
			char r = (char) wipf.getRandomInt(60);
			char g = (char) wipf.getRandomInt(10);
			char b = (char) wipf.getRandomInt(30);

			sb.append(String.format("%03d", i)); // Kein unsigned byte oder char in Java :(
			sb.append(r);
			sb.append(g);
			sb.append(b);

		}
		System.out.println("OUT");
		return sb.toString();

	}

}
