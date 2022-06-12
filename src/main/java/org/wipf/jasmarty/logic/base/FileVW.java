package org.wipf.jasmarty.logic.base;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.telegram.Telegram;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class FileVW {

	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("Files");

	/**
	 * @return
	 */
	public List<String> getAllFiles() {
		// File f = new File(Paths.get("").toAbsolutePath().toString() );
		List<String> sl = new LinkedList<>();
		File f = new File("files/");

		for (String sL : f.list()) {
			// Grafana Bilder nicht listen
			sl.add(sL);
		}

		return sl;
	}

	/**
	 * 
	 */
	public String getFilesForTelegram() {
		StringBuilder sb = new StringBuilder();

		for (String sF : getAllFiles()) {
			if (!sF.startsWith("grafana_")) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(sF);
			}
		}
		// Freier Platz in GB
		sb.append("\n\nFrei: " + new File("/").getUsableSpace() / Math.pow(1024, 3) + " GB");

		return sb.toString();
	}

	/**
	 * @param sFileName
	 */
	public File getFile(String sFileName) {
		if (isSaveFileName(sFileName)) {
			LOGGER.info("Get File " + sFileName);
			return new File("files/" + sFileName);
		}
		return null;
	}

	/**
	 * @return
	 */
	public File getDataBaseAsFile() {
		LOGGER.info("Get Database");
		return new File("jasmarty.db");
	}

	/**
	 * @param sFileName
	 * @return
	 */
	public boolean delFile(String sFileName) {
		if (isSaveFileName(sFileName)) {
			LOGGER.info("Delete File " + sFileName);
			return getFile(sFileName).delete();
		}
		return false;
	}

	/**
	 * @param sUrl
	 * @param sFileName
	 * @throws MalformedURLException
	 */
	public boolean saveFileToDisk(String sUrl, String sFileName) {
		LOGGER.info("Speichere File " + sUrl + " nach " + sFileName);
		try {
			URL urld = new URL(sUrl);
			ReadableByteChannel rbc = Channels.newChannel(urld.openStream());
			FileOutputStream fos = new FileOutputStream("files/" + sFileName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param t
	 * @return
	 */
	public String telegramToFile(Telegram t) {
		wipf.jsonToStringAsList(t.toJson());
		String sFilename = "files/" + "txt_" + t.getMid() + ".txt";

		try {
			FileUtils.writeStringToFile(new File(sFilename), t.getMessageFullWithoutFirstWord(),
					Charset.defaultCharset());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return sFilename;
	}

	/**
	 * @param sFileName
	 * @return
	 */
	private boolean isSaveFileName(String sFileName) {
		return (!sFileName.contains("..") && !sFileName.contains("/") && !sFileName.contains("\\"));
	}
}
