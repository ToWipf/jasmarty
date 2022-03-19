package org.wipf.jasmarty.logic.base;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class FileVW {

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
			}
		}
		// Freier Platz in GB
		sb.append("\n\nFrei: " + new File("/").getUsableSpace() / Math.pow(1024, 3) + " GB");

		return sb.toString();
	}

	/**
	 * @param sFilePath
	 */
	public File getFile(String sFilePath) {
		LOGGER.info("Get File " + sFilePath);
		return new File("files/" + sFilePath);
	}

	/**
	 * @param sName
	 * @return
	 */
	public boolean delFile(String sName) {
		LOGGER.info("Delete File " + sName);
		// Zur sicherheit
		if (!sName.contains("..")) {
			return getFile(sName).delete();
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

	public boolean upload(File f) {
		// f.path
		return false;
	}

}
