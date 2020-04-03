package org.wipf.wipfapp.logic.jasmarty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class JaSmartySend {

	@Inject
	JaSmartyConnect jaSmartyConnect;

	public void sendString(String s) {

		jaSmartyConnect.writeString(s);

	}

	public void writeLineToCache(Integer x, Integer y, String s) {
		jaSmartyConnect.lc.writeLine(x, y, s);
	}

	public void writeToCache(Integer x, Integer y, char c) {
		jaSmartyConnect.lc.write(x, y, c);
	}
}
