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

}
