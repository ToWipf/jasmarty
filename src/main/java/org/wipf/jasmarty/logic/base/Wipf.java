package org.wipf.jasmarty.logic.base;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.Base32;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class Wipf {

	private static final Logger LOGGER = Logger.getLogger("Wipf");

	public enum httpRequestType {
		GET, POST
	};

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
	 * @return
	 */
	public boolean getRandomBoolean() {
		Random wuerfel = new Random();
		return wuerfel.nextBoolean();
	}

	/**
	 * @return
	 */
	public String getRandomUUID() {
		return UUID.randomUUID().toString();
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
	 * @param sUrl
	 * @param sFilePathAndName
	 * @throws MalformedURLException
	 */
	public boolean downloadFile(String sUrl, String sFilePathAndName) {
		LOGGER.info("Donwload File " + sUrl + " nach " + sFilePathAndName);
		try {
			URL urld = new URL(sUrl);
			ReadableByteChannel rbc = Channels.newChannel(urld.openStream());
			FileOutputStream fos = new FileOutputStream("files/" + sFilePathAndName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 */
	public void printLogo() {
		LOGGER.info("__          ___        __");
		LOGGER.info("\\ \\        / (_)      / _|");
		LOGGER.info(" \\ \\  /\\  / / _ _ __ | |");
		LOGGER.info("  \\ \\/  \\/ / | | '_ \\|  _|");
		LOGGER.info("   \\  /\\  /  | | |_) | |");
		LOGGER.info("    \\/  \\/   |_| .__/|_|");
		LOGGER.info("               | |");
		LOGGER.info("               |_|");

	}

	/**
	 * @param sUrl
	 * @return
	 * @throws IOException
	 */
	public String httpRequest(httpRequestType method, String sUrl) throws IOException {
		URL url = new URL(sUrl.substring(0, Math.min(sUrl.length(), 4000)));

		HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		urlc.setRequestMethod(method.name());
		urlc.setRequestProperty("Accept", "*/*");
		urlc.setConnectTimeout(5000); // 5 Sek.
		urlc.setReadTimeout(60000); // 1 Min.

		if (method != httpRequestType.GET) {
			urlc.setDoOutput(true);
		}

		urlc.setAllowUserInteraction(false);

		// get result
		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		String l = null;
		StringBuilder sbOut = new StringBuilder();
		while ((l = br.readLine()) != null) {
			sbOut.append(l);
		}
		br.close();
		return sbOut.toString();
	}

	/**
	 * @param str
	 * @return
	 */
	public double doMathByString(String str) {
		try {
			return doMath(str);
		} catch (Exception e) {
			return -1;
		}
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
	 * @param s
	 * @return
	 */
	public String escapeStringSaveCode(String s) {
		// @formatter:off
		return s.replaceAll("\t", " ")
				.replaceAll("\\|", "_")
				.replaceAll("'", "_")
				.replaceAll("\"", "_")
				.replaceAll("\\{", "(")
				.replaceAll("\\}", ")")
				.replaceAll("\\\\", "")
				.trim();
		// @formatter:on
	}

	/**
	 * @param s
	 * @return
	 */
	public String escapeStringSatzzeichen(String s) {
		return s.replaceAll("\\?", "").replaceAll("\\!", "").replaceAll("\\.", "").trim();
	}

	/**
	 * @param value
	 * @return
	 */
	public String encodeUrlString(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			return "Fehler 51";
		}
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
			return (base32.encode(hasil));
		} catch (Exception e) {
			return "fail";
		}
	}

	/**
	 * @param sJson
	 * @return
	 */
	public String encrypt(String sJson) {
		try {
			JSONObject jo = new JSONObject(sJson);

			String sCryptKey = jo.getString("key");
			String sIn = jo.getString("data");

			SecretKeySpec secretKeySpec = new SecretKeySpec((sCryptKey).getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] hasil = cipher.doFinal(sIn.getBytes());
			// return Base64.getEncoder().encodeToString(hasil);
			Base32 base32 = new Base32();
			return (base32.encode(hasil));
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
			return "Fail";
		}
	}

	/**
	 * @param sJson
	 * @return
	 */
	public String decrypt(String sJson) {
		try {
			JSONObject jo = new JSONObject(sJson);

			String sCryptKey = jo.getString("key");
			String sIn = jo.getString("data");

			SecretKeySpec secretKeySpec = new SecretKeySpec((sCryptKey).getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			// byte[] hasil = cipher.doFinal(Base64.getDecoder().decode(sIn));
			Base32 base32 = new Base32();
			byte[] hasil = cipher.doFinal(base32.decode(sIn));
			return new String(hasil);
		} catch (Exception e) {
			return "Fail";
		}
	}

	/**
	 * @return
	 */
	public String getTime(String sPara) {
		try {
			SimpleDateFormat time = new SimpleDateFormat(sPara);
			return time.format(new Date());
		} catch (Exception e) {
			return "Fail 5";
		}
	}

	/**
	 * @param json
	 * @return
	 */
	public String jsonToStringAsList(JSONObject json) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = jsonToMap(json);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (sb.length() > 1) {
				sb.append("\n");
			}
			sb.append(entry.getKey() + ":" + entry.getValue());
		}
		return sb.toString();
	}

	/**
	 * Von:
	 * https://stackoverflow.com/questions/30663430/json-to-yaml-conversion-does-not-work/30682637
	 * 
	 * @param json
	 * @return
	 */
	private Map<String, Object> jsonToMap(JSONObject json) {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	/**
	 * @param object
	 * @return
	 */
	private Map<String, Object> toMap(JSONObject object) {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * @param array
	 * @return
	 */
	private List<Object> toList(JSONArray array) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

}
