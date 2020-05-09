package org.wipf.jasmarty.logic.jasmarty.actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.enterprise.context.ApplicationScoped;
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Control;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.FloatControl;
//import javax.sound.sampled.Line;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.Mixer;
//import javax.sound.sampled.Port;
//import javax.sound.sampled.SourceDataLine;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Tastatur {

	/**
	 * @param sText
	 * @param sTimeout
	 * @throws AWTException
	 */
	public void write(String sText, String sTimeout) throws AWTException {
		Integer nTime = Integer.valueOf(sTimeout);
		if (nTime != null) {
			write(sText, nTime);
		} else {
			write(sText, 100);
		}
	}

	/**
	 * @param sText
	 * @param nTimeout
	 * @throws AWTException
	 */
	public void write(String sText, int nTimeout) throws AWTException {
		Robot r = new Robot();
		int i = 0;
		char[] buchstaben = sText.toCharArray();
		while (i < sText.length()) {
			System.out.println("KeyEvent ist: " + buchstaben[i]);
			if (Character.isUpperCase(buchstaben[i])) {
				r.keyPress(KeyEvent.VK_SHIFT);
			}
			synchronized (r) {
				r.keyPress(Character.toUpperCase(buchstaben[i]));
				r.delay(nTimeout);
				r.keyRelease(Character.toUpperCase(buchstaben[i]));
			}
			if (Character.isUpperCase(buchstaben[i])) {
				r.keyRelease(KeyEvent.VK_SHIFT);
			}
			i++;
		}
	}

	/**
	 * Schreibt ein A
	 */
//	public void name() {
//		Robot r;
//		try {
//			r = new Robot();
//			r.keyPress(KeyEvent.VK_A);
//			r.keyRelease(KeyEvent.VK_A);
//		} catch (AWTException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public void vol() {
//
//		try {
//			namevc();
//		} catch (LineUnavailableException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		Port lineIn;
//		FloatControl volCtrl = null;
//		try {
//			Mixer mixer = AudioSystem.getMixer(null);
//			lineIn = (Port) mixer.getLine(Port.Info.SPEAKER);
//			lineIn.open();
//			volCtrl = (FloatControl) lineIn.getControl(
//
//					FloatControl.Type.VOLUME);
//
//			// Assuming getControl call succeeds,
//			// we now have our LINE_IN VOLUME control.
//		} catch (Exception e) {
//			System.out.println("VOLUME control: exception = " + e);
//		}
//		if (volCtrl != null) {
//			// ...
//			System.out.println("y");
//		}
//	}
//
//	public void namexx() throws LineUnavailableException {
//		Mixer.Info[] mi = AudioSystem.getMixerInfo();
//		for (Mixer.Info info : mi) {
//			System.out.println("info: " + info);
//			Mixer m = AudioSystem.getMixer(info);
//			System.out.println("mixer " + m);
//			Line.Info[] sl = m.getSourceLineInfo();
//			for (Line.Info info2 : sl) {
//				System.out.println("    info: " + info2);
//				Line line = AudioSystem.getLine(info2);
//				if (line instanceof SourceDataLine) {
//					SourceDataLine source = (SourceDataLine) line;
//
//					DataLine.Info i = (DataLine.Info) source.getLineInfo();
//					for (AudioFormat format : i.getFormats()) {
//						System.out.println("    format: " + format);
//					}
//				}
//			}
//		}
//	}
//
//	public void namevc() throws LineUnavailableException {
//		Mixer.Info[] mi = AudioSystem.getMixerInfo();
//		for (Mixer.Info info : mi) {
//
//			if (info.toString().indexOf("Realtek") != -1) {
//				Mixer m = AudioSystem.getMixer(info);
//
//				System.out.println("info: " + info);
//				System.out.println("mixer " + m);
//
//				Control[] c = m.getControls();
//				for (Control x : c) {
//					System.out.println("Contr: " + x.toString());
//				}
//
//				Line.Info[] sl = m.getSourceLineInfo();
//
//				for (Line.Info info2 : sl) {
//					System.out.println("    info: " + info2);
//					Line line = AudioSystem.getLine(info2);
//
//					if (line instanceof SourceDataLine) {
//						SourceDataLine source = (SourceDataLine) line;
//
//						DataLine.Info i = (DataLine.Info) source.getLineInfo();
//						for (AudioFormat format : i.getFormats()) {
//							System.out.println("    format: " + format);
//						}
//					}
//				}
//			}
//
//		}
//	}

}
