package org.wipf.jasmarty.logic.jasmarty.actions;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import com.qotsa.jni.controller.WinampController;

/**
 * @author user
 *
 */
@ApplicationScoped
public class Winamp {

	private static final Logger LOGGER = Logger.getLogger("Winamp");

	/**
	 * @param sInput
	 */
	public void control(String sInput) {
		try {
			switch (sInput.toLowerCase()) {
			case "play":
				WinampController.play();
				break;
			case "pause":
				WinampController.pause();
				break;
			case "stop":
				WinampController.stop();
				break;
			case "resume":
				WinampController.resume();
				break;
			case "nexttrack":
				WinampController.nextTrack();
				break;
			case "previoustrack":
				WinampController.previousTrack();
				break;
			case "increasevolume":
				WinampController.increaseVolume();
				break;
			case "decreaseVolume":
				WinampController.decreaseVolume();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			LOGGER.warn("control " + e);
		}
	}

	/**
	 * @param sInput
	 * @return
	 */
	public String getInfos(String sInput) {
		try {
			switch (sInput.toLowerCase()) {
			case "filename":
				return WinampController.getFileNamePlaying();
			case "title":
				return WinampController.getTitle();
			case "time":
				return ((Integer) WinampController.getTime(0)).toString();

			default:
				return "Fehler W1";
			}
		} catch (Exception e) {
			LOGGER.warn("getInfos " + e);
			return "";
		}
	}

}
