package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.MultipartUtility;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class TAppGrafana {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	SendAndReceive sendAndReceive;

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

		if (!(sTeil2_Zeit.contains("m") || sTeil2_Zeit.contains("h") || sTeil2_Zeit.contains("d"))) {
			return "Zeiteinheit fehlt (m,h,d)";
		}

		try {
			return sendGrafanaPicture(t.getChatID(), "ydVqZGkgk/heizung", sPanelId, sTeil2_Zeit);
		} catch (Exception e) {
			return "Fehler 8 " + e;
		}
	}

	/**
	 * @param sDashboard
	 * @param sPanel
	 * @param sTime
	 * @return
	 * @throws IOException
	 */
	private String sendGrafanaPicture(Integer nChatId, String sDashboard, String sPanel, String sTime)
			throws IOException {

//		http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3&from=now-7d&to=now
		URL URLGrafana = new URL("http://192.168.2.11:3000/render/d-solo/" + sDashboard + "?orgId=1&panelId=" + sPanel
				+ "&from=now-" + sTime + "&to=now");

		String sFilename = (nChatId + sDashboard + sPanel + sTime + ".png").replace('/', '-');

		ReadableByteChannel rbc = Channels.newChannel(URLGrafana.openStream());
		FileOutputStream fos = new FileOutputStream(sFilename);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		sendAndReceive.sendPictureToTelegram(nChatId, sFilename);
		return sFilename;
	}

	/**
	 * @param chatId
	 */
	public void testen(Integer chatId) {
		try {
			URL URLGrafana = new URL("http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3");

			ReadableByteChannel rbc = Channels.newChannel(URLGrafana.openStream());
			FileOutputStream fos = new FileOutputStream("photo.png");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();

			MultipartUtility multipart = new MultipartUtility("https://api.telegram.org/"
					+ wipfConfig.getConfParamString("telegrambot") + "/sendPhoto?chat_id=" + chatId, "UTF-8");
			// multipart.addFormField("param_name_1", "param_value");
			multipart.addFilePart("photo", new File("photo.png"));
			String response = multipart.finish();
			System.out.println(response);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
