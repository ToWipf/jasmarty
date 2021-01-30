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
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.jasmarty.extensions.Winamp;

@ApplicationScoped
public class Lcd12864PageConverter {

	@Inject
	Lcd12864Cache cache;
	@Inject
	Wipf wipf;
	@Inject
	Winamp winamp;

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
			cache.setScreen(buildPage(pd));
		}
	}

	/**
	 * curl -d
	 * '{"id":1,"name":"testseite1","static":[],"dynamic":[{"type":"TEXT","font":"FONT_57",
	 * "data":"11112222", "x":0,"y":0}]}' -X POST localhost:8080/lcd12864/savePage
	 * 
	 * 
	 * curl -d
	 * '{"id":1,"name":"testseite1","static":[],"dynamic":[{"type":"TEXT","font":"FONT_57",
	 * "data":"$time(dd MMMM yyyy)", "x":0,"y":0},{"type":"TEXT","font":"FONT_57",
	 * "data":"$time(dd MMMM yyyy HH-mm-ss)", "x":0,"y":20}]}' -X POST
	 * localhost:8080/lcd12864/savePage
	 * 
	 * @param page
	 */
	private Lcd12864Page buildPage(Lcd12864PageDescription pd) {
		Lcd12864Page lp = new Lcd12864Page();
		JSONArray jDynamic = pd.getDynamic();
		JSONArray jStatic = pd.getStatic();

		lp.setScreen(jStatic);

		for (Object o : jDynamic) {
			JSONObject jo = new JSONObject(o.toString());

			switch (jo.get("type").toString()) {
			case "TEXT":
				switch (jo.get("font").toString()) {
				case "FONT_57":
					lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_57), jo.getInt("x"), jo.getInt("y"),
							lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()));
					break;
				case "FONT_68":
					lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_68), jo.getInt("x"), jo.getInt("y"),
							lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()));
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

	/**
	 * Alle Variablen finden und und ersetzen
	 * 
	 * @param sLine
	 * @return
	 * @throws Exception
	 */
	private String searchAndReplaceVarsInString(String sLine) {
		String sOut = sLine;

		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = sLine.indexOf("$", lastIndex);

			if (lastIndex != -1) {
				lastIndex += 1;
				sOut = lineReplaceVars(sOut);
			}
		}
		return sOut;
	}

	/**
	 * @param sLine
	 * @return
	 */
	private String lineReplaceVars(String sLine) {
		if (sLine.length() < 3) {
			return sLine;
		}

		// Positionen bestimmen
		Integer nIndexStart = sLine.lastIndexOf('$');
		if (nIndexStart == -1) {
			return sLine;
		}
		Integer nIndexParaStart = sLine.lastIndexOf('(');
		if (nIndexParaStart == -1) {
			return sLine;
		}
		Integer nIndexEnd = sLine.indexOf(')', nIndexParaStart);
		if (nIndexEnd == -1) {
			return sLine;
		}

		String sBefore = sLine.substring(0, nIndexStart);
		String sCommand = sLine.substring(nIndexStart + 1, nIndexParaStart);
		String sParameter = sLine.substring(nIndexParaStart + 1, nIndexEnd);
		String sAfter = sLine.substring(nIndexEnd + 1, sLine.length());

		// Werte ermittelen
		switch (sCommand) {
		case "time":
			return sBefore + wipf.time(sParameter) + sAfter;
		case "math":
			return sBefore + wipf.doMathByString(sParameter) + sAfter;
		case "rnd":
			return sBefore + wipf.getRandomInt(sParameter) + sAfter;
		case "winamp":
			try {
				return sBefore + winamp.getInfos(sParameter) + sAfter;
			} catch (Exception e) {
				return "Winamp Fail";
			}
		case "ver":
			return sBefore + MainHome.VERSION + sAfter;
		default:
			return "Nicht gefunden: " + sCommand;
		}
	}
}
