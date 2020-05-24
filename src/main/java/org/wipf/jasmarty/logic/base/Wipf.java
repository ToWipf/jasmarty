package org.wipf.jasmarty.logic.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.codec.binary.Base32;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Wipf {

	/**
	 * @param ms
	 */
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nMax
	 * @return
	 */
	public int getRandomInt(int nMax) {
		Random wuerfel = new Random();
		return wuerfel.nextInt(nMax);
	}

	/**
	 * @param sMax
	 * @return
	 */
	public int getRandomInt(String sMax) {
		return getRandomInt(Integer.valueOf(sMax));
	}

	/**
	 * @param c
	 * @param times
	 * @return
	 */
	public String repeat(char c, int times) {
		if (times < 1) {
			return "";
		}
		return new String(new char[times]).replace('\0', c);
	}

	/**
	 * 
	 */
	public void printLogo() {
		System.out.println("__          ___        __");
		System.out.println("\\ \\        / (_)      / _|");
		System.out.println(" \\ \\  /\\  / / _ _ __ | |");
		System.out.println("  \\ \\/  \\/ / | | '_ \\|  _|");
		System.out.println("   \\  /\\  /  | | |_) | |");
		System.out.println("    \\/  \\/   |_| .__/|_|");
		System.out.println("               | |");
		System.out.println("               |_|");

	}

	/**
	 * @param str
	 * @return
	 */
	public double doMathByString(String str) {
		return doMath(str);
	}

	/**
	 * Von:
	 * https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
	 * 
	 * @param str
	 * @return
	 */
	private double doMath(final String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat) {
				while (ch == ' ')
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException("Unexpected: " + (char) ch);
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			// | number | functionName factor | factor `^` factor

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+'))
						x += parseTerm(); // addition
					else if (eat('-'))
						x -= parseTerm(); // subtraction
					else
						return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*'))
						x *= parseFactor(); // multiplication
					else if (eat('/'))
						x /= parseFactor(); // division
					else
						return x;
				}
			}

			double parseFactor() {
				if (eat('+'))
					return parseFactor(); // unary plus
				if (eat('-'))
					return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('[')) { // parentheses
					x = parseExpression();
					eat(']');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt"))
						x = Math.sqrt(x);
					else if (func.equals("sin"))
						x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos"))
						x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan"))
						x = Math.tan(Math.toRadians(x));
					else
						throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}

				if (eat('^'))
					x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getExternalIp() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (Exception e) {
			return "Fehler 21a";
		}
	}

	/**
	 * @param sIP
	 * @return
	 */
	public Boolean ping(String sIP) {
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
	public String shell(String sCommand) {
		// TODO timeout
		return "todo";
		/*
		 * ProcessBuilder processBuilder = new ProcessBuilder(); // Windows //
		 * processBuilder.command("cmd.exe", "/c", "ping -n 3 test.com"); // Linux
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
	 * @param s
	 * @return
	 */
	public String escapeString(String s) {
		return s.replaceAll("\n", "%0A").replaceAll(" ", "%20").replaceAll("\t", "%20").replaceAll("\\|", "%7C")
				.replaceAll("'", "%27").replaceAll("<", "_").replaceAll(">", "_").replaceAll("'", "_")
				.replaceAll("\"", "_").replaceAll("\\{", "(").replaceAll("\\}", ")");
	}

	/**
	 * @param sIn
	 * @return
	 */
	public String encrypt(String sIn, String sCryptKey) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec((sCryptKey).getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] hasil = cipher.doFinal(sIn.getBytes());
			// return Base64.getEncoder().encodeToString(hasil);

			Base32 base32 = new Base32();
			return (base32.encodeAsString(hasil));
		} catch (Exception e) {
			return "fail";
		}
	}

	/**
	 * @param sIn
	 * @return
	 */
	public String decrypt(String sIn, String sCryptKey) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec((sCryptKey).getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			// byte[] hasil = cipher.doFinal(Base64.getDecoder().decode(sIn));
			Base32 base32 = new Base32();
			byte[] hasil = cipher.doFinal(base32.decode(sIn));
			return new String(hasil);
		} catch (Exception e) {
			return "fail";
		}
	}

	/**
	 * @return
	 */
	public String time(String sPara) {
		try {
			SimpleDateFormat time = new SimpleDateFormat(sPara);
			return time.format(new Date());
		} catch (Exception e) {
			return "Fail 5";
		}
	}

}
