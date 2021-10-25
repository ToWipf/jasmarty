package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.MultipartUtility;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class TAppGrafana {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;

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
