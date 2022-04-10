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

	}

	/**
	 * 
	 */
	public List<TodoEntry> lasttest() {
		// Erstelle ein großen Objekt

		List<TodoEntry> lte = new LinkedList<>();
		for (int i = 0; i < 1000000; i++) {
			TodoEntry to = new TodoEntry();

			to.setId(i);
			to.setDate((int) (System.currentTimeMillis() / 1000));

			to.setEditBy("lasttest");
			to.setChatID(0);
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
