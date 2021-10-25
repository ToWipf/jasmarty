package org.wipf.jasmarty.logic.telegram;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

@ApplicationScoped
public class TAppGrafana {

	@Inject
	Wipf wipf;
	@Inject
	WipfConfig wipfConfig;

	public void testen() throws Exception {

//		URL website = new URL("http://user:user@192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3");
//		InputStream in = website.openStream();
//		File file = new File("tmpbild");
//		OutputStream outputStream = new FileOutputStream(file);
//		IOUtils.copy(in, outputStream);

		// FileUtils.copyURLToFile("http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3",
		// file);

		URL website = new URL("http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("photo.png");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		Unirest.setTimeouts(0, 0);

		//
		String sUrl = "https://api.telegram.org/" + wipfConfig.getConfParamString("telegrambot")
				+ "/sendPhoto?chat_id=798200105";
		System.out.println(sUrl);
		HttpResponse<String> response = Unirest.post(sUrl).field("photo", new File("photo.png")).asString();
		System.out.println(response.getBody());

		/// String x = wipf.httpRequest(httpRequestType.GET,
		// "http://192.168.2.11:3000/render/d-solo/ydVqZGkgk/heizung?orgId=1&panelId=3");
		// System.out.println(x);

		//

//	//	URL url = new URL("https://api.telegram.org/" + wipfConfig.getConfParamString("telegrambot")
//				+ "/sendPhoto?chat_id=798200105");
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setReadTimeout(10000);
//		conn.setConnectTimeout(15000);
//		conn.setRequestMethod("POST");
//		conn.setUseCaches(false);
//		conn.setDoInput(true);
//		conn.setDoOutput(true);
//		conn.setRequestProperty("Content-Type", "multipart/form-data;");
//		conn.getin("photo", in);
//		OutputStream outputStream = conn.getOutputStream();
//
//		byte[] buffer = new byte[4096];
//		int bytesRead;
//		while ((bytesRead = in.read(buffer)) != -1) {
//			outputStream.write(buffer, 0, bytesRead);
		// }

		// conn.setRequestProperty("Connection", "Keep-Alive");
		// conn.addRequestProperty("Content-length", file.length() + "");
		// conn.addRequestProperty("bild", "");

		// OutputStream os = conn.getOutputStream();
		// os.getChannel().
		// reqEntity.writeTo(conn.getOutputStream());
		// os.close();
		// conn.connect();

		// get result
//		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		String l = null;
//		StringBuilder sbOut = new StringBuilder();
//		while ((l = br.readLine()) != null) {
//			sbOut.append(l);
//		}
//		br.close();
//		System.out.println(sbOut.toString());

	}
}
