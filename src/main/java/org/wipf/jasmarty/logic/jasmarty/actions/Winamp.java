package org.wipf.jasmarty.logic.jasmarty.actions;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import com.qotsa.exception.InvalidHandle;
import com.qotsa.jni.controller.WinampController;

/**
 * @author user
 *
 */
@ApplicationScoped
public class Winamp {

	private static final Logger LOGGER = Logger.getLogger("Winamp");

	/**
	 * 
	 */
	public void doPlay() {
		try {
			WinampController.play();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("play " + e);
		}
	}

	/**
	 * 
	 */
	public void doPause() {
		try {
			WinampController.pause();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("pause " + e);
		}
	}

	/**
	 * 
	 */
	public void doStop() {
		try {
			WinampController.stop();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("stop " + e);
		}
	}

	/**
	 * 
	 */
	public void doUnstop() {
		try {
			WinampController.resume();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("VolDown " + e);
		}
	}

	/**
	 * 
	 */
	public void doNext() {
		try {
			WinampController.nextTrack();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("next " + e);
		}
	}

	/**
	 * 
	 */
	public void doLast() {
		try {
			WinampController.previousTrack();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("last " + e);
		}
	}

	/**
	 * 
	 */
	public void doVolUp() {
		try {
			WinampController.increaseVolume();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("VolUp " + e);
		}
	}

	/**
	 * 
	 */
	public void doVolDown() {
		try {
			WinampController.decreaseVolume();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("VolDown " + e);
		}
	}

	/**
	 * @return
	 */
	public String getFileNamePlaying() {
		try {
			return WinampController.getFileNamePlaying();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("VolDown " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	public String getTitle() {
		try {
			return WinampController.getTitle();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("Title " + e);
			return null;
		}
	}

	/**
	 * @return
	 */
	public String getTime() {
		try {
			return WinampController.getTitle();
		} catch (InvalidHandle e) {
			// TODO Auto-generated catch block
			LOGGER.warn("Time " + e);
			return null;
		}
	}
}
