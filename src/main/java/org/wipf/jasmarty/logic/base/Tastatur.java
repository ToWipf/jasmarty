package org.wipf.jasmarty.logic.base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

public class Tastatur {

	public void write(String s) {
		try {
			Robot r = new Robot();
			int i = 0;
			char[] buchstaben = s.toCharArray();
			while (i < s.length()) {
				System.out.println("KeyEvent ist: " + buchstaben[i]);
				if (Character.isUpperCase(buchstaben[i])) {
					r.keyPress(KeyEvent.VK_SHIFT);
				}
				synchronized (r) {
					r.keyPress(Character.toUpperCase(buchstaben[i]));
					r.delay(500);
					r.keyRelease(Character.toUpperCase(buchstaben[i]));
				}
				if (Character.isUpperCase(buchstaben[i])) {
					r.keyRelease(KeyEvent.VK_SHIFT);
				}
				i++;
			}
		} catch (AWTException e) {
			System.err.println(e);
		}
	}

	public void name() throws AWTException {
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_H);
		r.keyRelease(KeyEvent.VK_H);
		r.keyPress(KeyEvent.VK_A);
		r.keyRelease(KeyEvent.VK_A);
		r.keyPress(KeyEvent.VK_L);
		r.keyRelease(KeyEvent.VK_L);
		r.keyPress(KeyEvent.VK_L);
		r.keyRelease(KeyEvent.VK_L);
		r.keyPress(KeyEvent.VK_O);
		r.keyRelease(KeyEvent.VK_O);
	}

	public void vol() {
		Port lineIn;
		FloatControl volCtrl = null;
		try {
			Mixer mixer = AudioSystem.getMixer(null);
			lineIn = (Port) mixer.getLine(Port.Info.LINE_IN);
			lineIn.open();
			volCtrl = (FloatControl) lineIn.getControl(

					FloatControl.Type.VOLUME);

			// Assuming getControl call succeeds,
			// we now have our LINE_IN VOLUME control.
		} catch (Exception e) {
			System.out.println("Failed trying to find LINE_IN" + " VOLUME control: exception = " + e);
		}
		if (volCtrl != null) {
			// ...
		}

	}

}
