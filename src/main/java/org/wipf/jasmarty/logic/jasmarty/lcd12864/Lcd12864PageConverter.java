package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font.Lcd12864fontType;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page.lineAlignment;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageDescription;

@ApplicationScoped
public class Lcd12864PageConverter {

	@Inject
	Lcd12864Cache cache;

	private Lcd12864PageDescription pd;

	/**
	 * @param pd
	 */
	public void setPageDescription(Lcd12864PageDescription pd) {
		this.pd = pd;
	}

	/**
	 * 
	 */
	public void refreshCurrentPage() {
		if (pd != null) {
			System.out.println("re");
			cache.setScreen(buildPage(pd));
		}
	}

	/**
	 * curl -d '{"id":1,"name":"testseite1","static":[],"dynamic":[{"type":"TEXT",
	 * "font":"FONT_57", "data":"11112222", "x":0,"y":0}]}' -X POST
	 * localhost:8080/lcd12864/savePage
	 * 
	 * 
	 * @param page
	 */
	private Lcd12864Page buildPage(Lcd12864PageDescription pd) {
		Lcd12864Page lp = new Lcd12864Page();
		JSONArray jDynamic = pd.getDynamic();
		JSONArray jStatic = pd.getStatic(); // TODO

		for (Object o : jDynamic) {
			JSONObject jo = new JSONObject(o.toString());

			switch (jo.get("type").toString()) {
			case "TEXT":
				switch (jo.get("font").toString()) {
				case "FONT_57":
					lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_57), jo.getInt("x"), jo.getInt("y"),
							lineAlignment.CUSTOM, jo.get("data").toString());
					break;
				case "FONT_68":
					lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_68), jo.getInt("x"), jo.getInt("y"),
							lineAlignment.CUSTOM, jo.get("data").toString());
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}
		}

		return lp;
	}

}
