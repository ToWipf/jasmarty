package org.wipf.wipfapp.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.wipfapp.logic.jasmarty.JaSmartyConnect;

/**
 * @author wipf
 *
 */
@Path("/lcd")
public class lcd {

	@Inject
	JaSmartyConnect jaSmartyConnect;

	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public String open() {
		return "{\"open\":\"" + jaSmartyConnect.startPort() + "\"}";
	}

	@GET
	@Path("/close")
	@Produces(MediaType.APPLICATION_JSON)
	public String close() {
		return "{\"close\":\"" + jaSmartyConnect.close() + "\"}";
	}

	@GET
	@Path("/refreshNOW")
	@Produces(MediaType.TEXT_PLAIN)
	public String refreshDisplay() {
		jaSmartyConnect.refreshDisplay();
		return "{}";
	}

	@GET
	@Path("/refreshON")
	@Produces(MediaType.TEXT_PLAIN)
	public String refreshDisplayON() {
		jaSmartyConnect.startRefreshDisplay();
		return "{}";
	}

	@GET
	@Path("/refreshOFF")
	@Produces(MediaType.TEXT_PLAIN)
	public String refreshDisplayOFF() {
		jaSmartyConnect.stopRefreshDisplay();
		return "{}";
	}

	@GET
	@Path("/resetFail")
	@Produces(MediaType.TEXT_PLAIN)
	public String resetLcdFaild() {
		jaSmartyConnect.resetLcdFaild();
		return "{}";
	}

	@GET
	@Path("/write/{x}/{y}/{str}")
	@Produces(MediaType.TEXT_PLAIN)
	public String cWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		jaSmartyConnect.writeLineToCache(x, y, s.toCharArray());
		return "{}";
	}

	@GET
	@Path("/chIst")
	@Produces(MediaType.TEXT_PLAIN)
	public String chIst() {
		return jaSmartyConnect.getCachIstAsString();

	}

	@GET
	@Path("/chSoll")
	@Produces(MediaType.TEXT_PLAIN)
	public String chSoll() {
		return jaSmartyConnect.getCachSollAsString();

	}

//	@GET
//	@Path("/writeAscii/{int}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String writeAscii(@PathParam("int") Integer n) {
//		return "{\"test\":\"" + jaSmartyConnect.writeAscii(n) + "\"}";
//	}

//	@GET
//	@Path("/pos/{x}/{y}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
//		jaSmartyConnect.setCursor(x, y);
//		return "{}";
//	}

//	@GET
//	@Path("/cls")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String cls() {
//		jaSmartyConnect.clearScreen();
//		return "{}";
//	}

//	@GET
//	@Path("/write/{s}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String close(@PathParam("s") String s) {
//		jaSmartySend.sendString(s);
//		return "{}";
//	}

}
