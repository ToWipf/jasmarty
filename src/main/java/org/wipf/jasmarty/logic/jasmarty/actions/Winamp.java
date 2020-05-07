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
	 * @param sParameter
	 */
	public void control(String sInput, String sParameter) {
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
			case "volumeupproz":
				WinampController.decreaseVolumePercent(Integer.valueOf(sParameter));
				break;
			case "volumedownproz":
				WinampController.decreaseVolumePercent(Integer.valueOf(sParameter));
				break;
			case "volumeset":
				WinampController.setVolume(Integer.valueOf(sParameter));
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
	 * @throws Exception
	 */
	public String getInfos(String sInput) throws Exception {
		switch (sInput.toLowerCase()) {
		case "filename":
			return WinampController.getFileNamePlaying();
		case "title":
			return WinampController.getTitle();
		case "titlename":
			return WinampController.getTitle().substring(WinampController.getTitle().indexOf('.') + 2);
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

	}

}
