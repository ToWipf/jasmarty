package org.wipf.wipfapp.logic.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
public class MWipf {

	private static final Logger LOGGER = Logger.getLogger("MWipf");

	/**
	 * 
	 */
	public static void runGc() {
		System.gc();
	}

	/**
	 * @param sInput
	 * @return
	 */
	public static Response genResponse(String sInput) {
		return Response.ok(sInput).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
	}

	/**
	 * @param b
	 * @return
	 */
	public static Response genResponse(Boolean b) {
		return Response.ok(b.toString()).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
	}

	/**
	 * @param i in millisec
	 */
	public static void sleep(Integer i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param s
	 * @return
	 */
	public static String escapeString(String s) {
		return s.replaceAll("\n", "%0A").replaceAll(" ", "%20").replaceAll("\t", "%20").replaceAll("\\|", "%7C")
				.replaceAll("'", "%27").replaceAll("<", "_").replaceAll(">", "_").replaceAll("'", "_")
				.replaceAll("\"", "_").replaceAll("\\{", "(").replaceAll("\\}", ")");
	}

	/**
	 * @param n
	 * @return Number als Symbol
	 */
	public static String numberToSymbol(Integer n) {
		// 1⃣
		if (n <= 9) {
			return n.toString() + "%E2%83%A3";
		}
		return null;
	}

	/**
	 * @return true or false
	 */
	public boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	/**
	 * @param nMax
	 * @return Zufallszahl
	 */
	public static int getRandomInt(int nMax) {
		Random wuerfel = new Random();
		return wuerfel.nextInt(nMax);
	}

	/**
	 * @return
	 */
	public static String uhr() {
		SimpleDateFormat uhr = new SimpleDateFormat("HH:mm:ss");
		return uhr.format(new Date());
	}

	/**
	 * @return
	 */
	public static String date() {
		SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy");
		return date.format(new Date());

	}

	/**
	 * @return
	 */
	public static String dayName() {
		SimpleDateFormat date = new SimpleDateFormat("EEEE");
		return date.format(new Date());

	}

	/**
	 * @return
	 */
	public static String dateTime() {
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return date.format(new Date());

	}

	/**
	 * @return
	 */
	public static String dateTimeMs() {
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss;SSS");
		return date.format(new Date());

	}

	/**
	 * @param sIP
	 * @return
	 */
	public static Boolean ping(String sIP) {
		try {
			InetAddress address = InetAddress.getByName(sIP);
			return address.isReachable(10000);

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param sCommand
	 */
	public static String shell(String sCommand) {
		// TODO timeout
		return "todo";
		/*
		 * ProcessBuilder processBuilder = new ProcessBuilder(); // Windows //
		 * processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com"); // Linux
		 * processBuilder.command(sCommand); StringBuilder sb = new StringBuilder();
		 * 
		 * try { Process process = processBuilder.start(); BufferedReader reader = new
		 * BufferedReader(new InputStreamReader(process.getInputStream()));
		 * 
		 * String line; while ((line = reader.readLine()) != null) { //
		 * System.out.println(line); sb.append(line); }
		 * 
		 * int exitCode = process.waitFor(); //
		 * System.out.println("\nExited with error code : " + exitCode);
		 * sb.append("\nExited with error code : " + exitCode);
		 * 
		 * } catch (IOException e) { e.printStackTrace(); } catch (InterruptedException
		 * e) { e.printStackTrace(); } return sb.toString();
		 */
	}

	/**
	 * @param sCommand
	 */
	public static String donmap() {
		ProcessBuilder processBuilder = new ProcessBuilder();

		processBuilder.command("/root/wipfapp/nmap.sh");
		StringBuilder sb = new StringBuilder();

		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) { //
				sb.append(line);
			}
			int exitCode = process.waitFor(); //
			sb.append("\nExited with error code : " + exitCode);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	/**
	 * @return
	 */
	public static String getExternalIp() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.warn("getExternalIp" + e);
			return null;
		}

	}

}
