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
			case "volumeup":
				WinampController.increaseVolume();
				break;
			case "volumedown":
				WinampController.decreaseVolume();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			LOGGER.warn("control " + sInput + " -> " + e);
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
			case "titlename":
				return WinampController.getTitle().substring(WinampController.getTitle().indexOf('.'));
			case "timefull":
				return ((Integer) (WinampController.getTime(WinampController.TIMELENGTH))).toString();
			case "time":
				return ((Integer) (WinampController.getTime(WinampController.CURRENTTIME) / 1000)).toString();
			case "timerem":
				return ((Integer) (WinampController.getTime(WinampController.TIMELENGTH)
						- WinampController.getTime(WinampController.CURRENTTIME) / 1000)).toString();
			case "status":
				return ((Integer) WinampController.getStatus()).toString();
			default:
				return "Fehler W1";
			}
		} catch (Exception e) {
			LOGGER.warn("getInfos " + e);
			return "";
		}
	}

}
