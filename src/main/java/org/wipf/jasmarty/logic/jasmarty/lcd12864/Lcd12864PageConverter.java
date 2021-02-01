package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Font.Lcd12864fontType;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page.lineAlignment;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase.pixelType;
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

	private Lcd12864PageDescription lpdCurrentCache;

	/**
	 * @param pd
	 */
	public void setPageDescription(Lcd12864PageDescription pd) {
		this.lpdCurrentCache = pd;
	}

	/**
	 * 
	 */
	public void refreshCurrentPageToCache() {
		cache.setScreen(buildPage(lpdCurrentCache));
	}

	/**
	 * @return
	 */
	public int getCurrentTimeoutime() {
		return lpdCurrentCache.getTimeouttime();
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
	private Lcd12864Page buildPage(Lcd12864PageDescription lpd) {
		Lcd12864Page lp = new Lcd12864Page();
		JSONArray jDynamic = lpd.getDynamic();
		JSONArray jStatic = lpd.getStatic();

		// Hintergrund setzen
		lp.setScreen(jStatic);

		if (jDynamic != null) {
			for (Object o : jDynamic) {
				JSONObject jo = new JSONObject(o.toString());

				switch (jo.get("type").toString()) {
				case "TEXT":
					switch (jo.get("font").toString()) {
					case "FONT_57_ON":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_57), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.ON);
						break;
					case "FONT_68_ON":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_68), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.ON);
						break;
					case "FONT_57_OFF":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_57), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.OFF);
						break;
					case "FONT_68_OFF":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_68), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.OFF);
						break;
					case "FONT_57_INVERT":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_57), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.INVERT);
						break;
					case "FONT_68_INVERT":
						lp.drawString(new Lcd12864Font(Lcd12864fontType.FONT_68), jo.getInt("x"), jo.getInt("y"),
								lineAlignment.CUSTOM, searchAndReplaceVarsInString(jo.get("data").toString()),
								pixelType.INVERT);
						break;
					default:
						break;
					}
					break;
				case "CIRCLE_EMPTY":
					switch (jo.get("font").toString()) {
					case "ON":
						lp.drawCircle(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.ON);
						break;
					case "OFF":
						lp.drawCircle(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.OFF);
						break;
					case "INVERT":
						lp.drawCircle(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.INVERT);

						break;
					default:
						break;
					}
					break;
				case "CIRCLE_FILL":
					switch (jo.get("font").toString()) {
					case "ON":
						lp.drawCircleFill(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.ON);
						break;
					case "OFF":
						lp.drawCircleFill(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.OFF);
						break;
					case "INVERT":
						lp.drawCircleFill(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.INVERT);
						break;
					default:
						break;
					}
					break;

				case "LINE_V":
					switch (jo.get("font").toString()) {
					case "ON":
						lp.drawLineV(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.ON);
						break;
					case "OFF":
						lp.drawLineV(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.OFF);
						break;
					case "INVERT":
						lp.drawLineV(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.INVERT);
						break;
					default:
						break;
					}
					break;

				case "LINE_H":
					switch (jo.get("font").toString()) {
					case "ON":
						lp.drawLineH(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.ON);
						break;
					case "OFF":
						lp.drawLineH(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.OFF);
						break;
					case "INVERT":
						lp.drawLineH(jo.getInt("x"), jo.getInt("y"), jo.getInt("data"), pixelType.INVERT);
						break;
					default:
						break;
					}
					break;

				default:
					break;
				}
			}
		}

		return lp;
	}

	/**
	 * 
	 */
	public void loadDefaultPage() {
		Lcd12864PageDescription lpdDefault = new Lcd12864PageDescription();
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("type", "TEXT");
		jo.put("font", "FONT_68_ON");
		jo.put("data", "Jasmarty " + MainHome.VERSION);
		jo.put("x", 30);
		jo.put("y", 32);
		ja.put(jo);

		lpdDefault.setId(0);
		lpdDefault.setName("Startseite");
		lpdDefault.setTimeouttime(10000);
		lpdDefault.setStatic("[]");
		lpdDefault.setDynamic(ja);

		this.setPageDescription(lpdDefault);
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
