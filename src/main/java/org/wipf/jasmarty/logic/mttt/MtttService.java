package org.wipf.jasmarty.logic.mttt;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

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
		List<Byte> bl = new LinkedList<>();
		Array a;

		byte[] bray = new byte[4];

		System.out.println("IN");
		for (int i = 0; i < 225; i++) {
			char r = 100;// wipf.getRandomInt(30);
			char g = 0;
			char b = 0;// wipf.getRandomInt(30);

			// int value = Integer.valueOf("254");
			// byte byteValue = (byte) value;
			byte test = (byte) 240;
			// short ah = 200;
			System.out.println(test & 0xFF);
			// byte asd = (test & 0xFF);

			// byte xx = -123;
			// (( https://www.javatpoint.com/java-convert-bytes-to-unsigned-bytes))

//			sb.append(i & 0xFF);
			sb.append(String.format("%03d", i));
			sb.append(r);
			sb.append(g);
			sb.append(b);
//			bray[0] = converter(128);
//			bray[1] = converter(250);
//			bray[2] = converter(0);
//			bray[3] = converter(0);

//			bl.add(byteconverter(i));
//			bl.add(byteconverter(r));
//			bl.add(byteconverter(g));
//			bl.add(byteconverter(b));
		}
		System.out.println("OUT");
		return sb.toString();
		// return bl.toString();
		// return sb.toString();

//		List<Character> cl = new LinkedList<>();
//		System.out.println("IN");
//		for (char i = 0; i < 1; i++) {
//			char r = 240;// wipf.getRandomInt(30);
//			char g = 0;// 10;
//			char b = 0;// wipf.getRandomInt(30);
//
//			cl.add(i);
//			cl.add((char) (r & 0xFF));
//			cl.add(g);
//			cl.add(b);
//			System.out.println("ZEILE");
//		}
//		System.out.println("OUT");
//
//		// 
//		return cl;
	}

//	/**
//	 * @return
//	 */
//	public String getTestdata() {
//		StringBuilder sb = new StringBuilder();
//
//		System.out.println("IN");
//		for (int i = 0; i < 225; i++) {
//			int r = 0;// wipf.getRandomInt(30);
//			int g = 0;// 10;
//			int b = 1;// wipf.getRandomInt(30);
//
//			sb.append(String.format("%03d", i) + String.format("%03d", r) + String.format("%03d", g) + String.format("%03d", b) + "\n");
//		}
//		System.out.println("OUT");
//
//		return sb.toString();
//	}
}
