package org.wipf.jasmarty.logic.base;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.wipf.jasmarty.datatypes.telegram.TodoEntry;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class WipfDebug {

	@Inject
	SqlLite sqlLite;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Test");
		code();

	}

	public static void code() {
		int nAnz = 0;
		int nMAXZAHL = 7;

		for (int a = 0; a <= nMAXZAHL; a++) {
			for (int b = 0; b <= nMAXZAHL; b++) {
				for (int c = 0; c <= nMAXZAHL; c++) {
					for (int d = 0; d <= nMAXZAHL; d++) {

						if (isGerade(a) && isGerade(b) && !isGerade(c) && d == 0) {

							if (!(a == b || a == c || a == d || b == c || b == d || c == d)) {
								// keine doppelten
								nAnz++;
								System.out.println(a + " " + b + " " + c + " " + d);
							}

						}

					}
				}
			}
		}
		System.err.println(nAnz);

	}

	private static boolean isGerade(int n) {
		return (n == 0 || n == 2 || n == 4 || n == 6 || n == 8);
	}

	/**
	 * 
	 */
	public List<TodoEntry> lasttest() {
		// Erstelle ein großen Objekt

		List<TodoEntry> lte = new LinkedList<>();
		for (int i = 0; i < 10000; i++) {
			TodoEntry to = new TodoEntry();

			to.setId(i);
			to.setDate((int) (System.currentTimeMillis() / 1000));

			to.setEditBy("lasttest");
			to.setChatID(0l);
			to.setType("");
			to.setActive("NEW");
			to.setData(UUID.randomUUID().toString());

			lte.add(to);
		}
		return lte;
	}

	/**
	 * @return
	 */
	public JSONArray lasttestAsJson() {
		JSONArray ja = new JSONArray();
		try {
			for (TodoEntry tItem : lasttest()) {
				ja.put(tItem.toJson());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ja;
	}
}
