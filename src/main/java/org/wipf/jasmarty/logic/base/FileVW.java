package org.wipf.jasmarty.logic.base;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class FileVW {

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
		return new File("files/" + sFilePath);
	}

	/**
	 * @param sName
	 * @return
	 */
	public boolean delFile(String sName) {
		// Zur sicherheit
		if (!sName.contains("..")) {
			return getFile(sName).delete();
		}
		return false;
	}

}
