package org.wipf.jasmarty.logic.base;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SoundMeter {

	JFrame j;

	public SoundMeter() {
		j = new JFrame("SoundMeter");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setLayout(new BoxLayout(j.getContentPane(), BoxLayout.Y_AXIS));
		printMixersDetails();
		j.setVisible(true);
	}

	public void printMixersDetails() {
		javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		System.out.println("There are " + mixers.length + " mixer info objects");
		for (int i = 0; i < mixers.length; i++) {
			Mixer.Info mixerInfo = mixers[i];
			System.out.println("Mixer Name:" + mixerInfo.getName());
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineinfos = mixer.getTargetLineInfo();
			for (Line.Info lineinfo : lineinfos) {
				System.out.println("line:" + lineinfo);
				try {
					Line line = mixer.getLine(lineinfo);
					line.open();
					if (line.isControlSupported(FloatControl.Type.VOLUME)) {
						FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
						System.out.println("Volume:" + control.getValue());
						JProgressBar pb = new JProgressBar();
						// if you want to set the value for the volume 0.5 will be 50%
						// 0.0 being 0%
						// 1.0 being 100%
						control.setValue((float) 0.5);
						int value = (int) (control.getValue() * 100);
						pb.setValue(value);
						j.add(new JLabel(lineinfo.toString()));
						j.add(pb);
						j.pack();
					}
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new SoundMeter();
	}
}