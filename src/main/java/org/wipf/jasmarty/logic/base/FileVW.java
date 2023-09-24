package org.wipf.jasmarty.logic.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
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
	public List<String> listAllFiles() {
		// File f = new File(Paths.get("").toAbsolutePath().toString() );
		List<String> sl = new LinkedList<>();
		File f = new File("files/");

		for (String sL : f.list()) {
			// Grafana Bilder nicht listen
			if (!sL.startsWith(".")) {
				sl.add(sL);
			}
		}

		return sl;
	}

	/**
	 * 
	 */
	public String getFilesForTelegram() {
		StringBuilder sb = new StringBuilder();

		for (String sF : listAllFiles()) {
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
			// LOGGER.info("Get File " + sFileName);
			return new File("files/" + sFileName);
		}
		return null;
	}

	/**
	 * @param originalImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 * @throws IOException
	 */
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}

	/**
	 * @param size
	 * @param sName
	 * @return
	 */
	public File getImageBySize(Integer size, String sName) {
		File f = getFile(sName);

		if (f == null) {
			return null;
		}

		File fout = getFile("." + sName + "_" + size);

		if (fout.exists()) {
			return fout;
		} else {
			try {
				BufferedImage in = ImageIO.read(f);
				BufferedImage img = resizeImage(in, size, (int) (((double) size / (double) in.getWidth()) * in.getHeight()));

				fout.createNewFile();
				ImageIO.write(img, "jpg", fout);
				return fout;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			LOGGER.error("Datei konnte nicht gespeichert werden! " + sUrl + " - " + sFileName + " - " + e);
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
			FileUtils.writeStringToFile(new File(sFilename), t.getMessageFullWithoutFirstWord(), Charset.defaultCharset());
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

	/**
	 * @param sFileName
	 * @param f
	 */
	public void saveFile(String sFileName, File f) {
		String sFN = sFileName.replaceAll("\\.\\.", "").replaceAll("/", "");
		if (wipf.isFilename(sFN)) {
			LOGGER.info("Upload - Saveing: " + f.getPath() + " to " + "files/" + sFN);
			// f.renameTo(new File("files/" + sFN));

			try {
				Files.move(Paths.get(f.getPath()), Paths.get("files/" + sFN), StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {
				LOGGER.warn("Upload Fail - Filename: " + sFN + " - " + e);
			}
		} else {
			LOGGER.warn("Kein Upload - Filename: " + sFN);
		}
	}

}
