package org.wipf.jasmarty.logic.jasmarty.extensions;

import javax.enterprise.context.ApplicationScoped;

import com.qotsa.exception.InvalidHandle;
import com.qotsa.jni.controller.WinampController;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Winamp {

	/**
	 * @param sInput
	 * @param sParameter
	 * @throws Exception
	 */
	public void control(String sInput, String sParameter) throws Exception {
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
		case "start":
			WinampController.run();
			break;
		default:
			break;
		}
	}

	/**
	 * @param sInput
	 * @return
	 * @throws Exception
	 */
	public String getInfos(String sInput) throws Exception {
		try {
			switch (sInput.toLowerCase()) {
			case "filename":
				return WinampController.getFileNamePlaying();
			case "title":
				String s = WinampController.getTitle();
				return s.substring(s.indexOf('.') + 2, s.lastIndexOf('-'));
			case "titleraw":
				return WinampController.getTitle();
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
				return "Fail 4";
			}
		} catch (InvalidHandle e) {
			// Bei Winamp InvalidHandle nichts anzeigen -> da Winamp nicht läuft
			return "";
		}

	}

}
