package org.wipf.jasmarty.logic.telegram;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.FileVW;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class TAppGrafana {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	TSendAndReceive sendAndReceive;
	@Inject
	FileVW fileVw;

	public String telegramMenuehHeizung(Telegram t) {
		String sPanelId = "";
		String sTeil1_Was = t.getMessageStringPartLow(1);
		String sTeil2_Zeit = t.getMessageStringPartLow(2);
		if (sTeil1_Was == null || sTeil2_Zeit == null) {
			// @formatter:off
				return 
					"Syntax:\n" + 
					"heizung <Panel> <Zeit>\n" +
					"Panels:\n" +
					"- tg/Temperaturengesamt\n" +
					"- de/Digitaleingänge\n" +
					"- a/Ausen\n" +
					"- wt/Wasser\n" +
					"- fb/Fusboden\n" +
					"- fbd/Fusbodendifferenz\n" +
					"\n" +
					"Zeit (die letzten xx) Beispiele:\n" +
					"- 15m (Minuten)\n" +
					"- 6h (Stunden)\n" +
					"- 1d (Tage)\n";
			// @formatter:on
		}

		switch (sTeil1_Was) {
		case "tg":
		case "temperaturengesamt":
			sPanelId = "3";
			break;
		case "de":
		case "digitaleingänge":
			sPanelId = "2";
			break;
		case "a":
		case "ausen":
			sPanelId = "9";
			break;
		case "wt":
		case "wasser":
			sPanelId = "8";
			break;
		case "fb":
		case "fusboden":
			sPanelId = "7";
			break;
		case "fbd":
		case "fusbodendifferenz":
			sPanelId = "23";
			break;
		default:
			sPanelId = "0";
			return "Panel id nicht gefunden";
		}

		if (validateInput(sTeil1_Was, sTeil2_Zeit)) {
			try {
				return sendGrafanaPictureToTelegram(t.getChatID(), "ydVqZGkgk/heizung", sPanelId, sTeil2_Zeit);
			} catch (Exception e) {
				return "Fehler 8 " + e;
			}
		} else {
			return "Fehlerhafte Eingabe\nHilfe mit 'Heizung'";
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramMenueDev(Telegram t) {
		String sPanelId = "";
		String sTeil1_Was = t.getMessageStringPartLow(1);
		String sTeil2_Zeit = t.getMessageStringPartLow(2);
		if (sTeil1_Was == null || sTeil2_Zeit == null) {
			// @formatter:off
				return 
					"Syntax:\n" + 
					"dev <Panel> <Zeit>\n" +
					"Panels:\n" +
					"- g\n" +
					"\n" +
					"Zeit (die letzten xx) Beispiele:\n" +
					"- 15m (Minuten)\n" +
					"- 6h (Stunden)\n" +
					"- 1d (Tage)\n";
			// @formatter:on
		}

		switch (sTeil1_Was) {
		case "g":
			sPanelId = "60";
			break;
		default:
			sPanelId = "0";
			return "Panel id nicht gefunden";
		}

		if (validateInput(sTeil1_Was, sTeil2_Zeit)) {
			try {
				return sendGrafanaPictureToTelegram(t.getChatID(), "FYv3KbWgk/netzwerk", sPanelId, sTeil2_Zeit);
			} catch (Exception e) {
				return "Fehler 8 " + e;
			}
		} else {
			return "Fehlerhafte Eingabe\nHilfe mit 'dev'";
		}

	}

	/**
	 * @param sTeil1_Was
	 * @param sTeil2_Zeit
	 * @return
	 */
	private Boolean validateInput(String sTeil1_Was, String sTeil2_Zeit) {
		if (sTeil1_Was.equals("0") || sTeil1_Was.equals("")) {
			return false;
		}
		if (!(sTeil2_Zeit.contains("m") || sTeil2_Zeit.contains("h") || sTeil2_Zeit.contains("d"))) {
			return false;
		}
		return true;
	}

	/**
	 * @param sDashboard
	 * @param sPanel
	 * @param sTime
	 * @return
	 * @throws IOException
	 */
	private String sendGrafanaPictureToTelegram(Long nChatId, String sDashboard, String sPanel, String sTime)
			throws IOException {

//		http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3&from=now-7d&to=now
		String sFilename = "grafana_" + (nChatId + sDashboard + sPanel + sTime + ".png").replace('/', '-');
		fileVw.saveFileToDisk("http://192.168.2.11:3000/render/d-solo/" + sDashboard + "?orgId=1&panelId=" + sPanel
				+ "&from=now-" + sTime + "&to=now", sFilename);

		sendAndReceive.sendDocumentToTelegram(nChatId, sFilename);

		// TODO grafana Bilder löschen
		return sFilename;
	}

}
