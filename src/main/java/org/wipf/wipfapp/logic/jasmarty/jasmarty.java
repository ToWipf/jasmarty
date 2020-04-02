package org.wipf.wipfapp.logic.jasmarty;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class jasmarty {

	private SerialPort sp;

	/**
	 * @param s
	 * @return
	 */
	public String send(Integer s) {
		System.out.println("send" + s);

		try {
			sp.getOutputStream().write(s.byteValue());
			sp.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "ok";
	}

	/**
	 * @return
	 */
	public String open() {
		sp = SerialPort.getCommPort("COM10");
		sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

		if (sp.openPort()) {
			System.out.println("Port is open");
		} else {
			System.out.println("Failed to open port");
			return "fail";
		}
		return "ok";
	}

	/**
	 * @return
	 */
	public String close() {
		if (sp.closePort()) {
			System.out.println("Port is closed");
		} else {
			System.out.println("Failed to close port");
			return "fail";
		}
		return "ok";
	}
}
