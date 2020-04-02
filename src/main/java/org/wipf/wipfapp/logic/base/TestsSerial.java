package org.wipf.wipfapp.logic.base;

import javax.enterprise.context.RequestScoped;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@RequestScoped
public class TestsSerial {

	/**
	 * @return
	 */
	public String test() {
		try {
			return doTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fail";
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String doTest() throws Exception {
		SerialPort sp = SerialPort.getCommPort("COM10"); // device name TODO: must be changed
		sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

		if (sp.openPort()) {
			System.out.println("Port is open :)");
		} else {
			System.out.println("Failed to open port :(");
			return "fail";
		}
		Thread.sleep(1000);

		for (Integer i = 0; i < 5; ++i) {
			sp.getOutputStream().write(i.byteValue());
			sp.getOutputStream().flush();
			System.out.println("Sent number: " + i);
			Thread.sleep(1000);
		}

		System.out.println("mytest");
		byte[] buffer = "4".getBytes();
		long bytesToWrite = 8;
		sp.writeBytes(buffer, bytesToWrite);

		byte[] buffer2 = "Ein Test String - Wipf".getBytes();
		sp.writeBytes(buffer2, buffer2.length);

		if (sp.closePort()) {
			System.out.println("Port is closed :)");
		} else {
			System.out.println("Failed to close port :(");
			return "fail2";
		}

		return "ok";

	}

}