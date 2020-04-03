package org.wipf.wipfapp.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.wipfapp.logic.jasmarty.JaSmartyConnect;
import org.wipf.wipfapp.logic.jasmarty.JaSmartySend;

/**
 * @author wipf
 *
 */
@Path("/lcd")
public class RestJaSmarty {

	@Inject
	JaSmartyConnect jaSmartyConnect;
	@Inject
	JaSmartySend jaSmartySend;

	@GET
	@Path("/writeAscii/{int}")
	@Produces(MediaType.APPLICATION_JSON)
	public String writeAscii(@PathParam("int") Integer n) {
		return "{\"test\":\"" + jaSmartyConnect.writeAscii(n) + "\"}";
	}

	@GET
	@Path("/pos/{x}/{y}")
	@Produces(MediaType.APPLICATION_JSON)
	public String pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		jaSmartyConnect.setCursor(x, y);
		return "{}";
	}

	@GET
	@Path("/cls")
	@Produces(MediaType.APPLICATION_JSON)
	public String cls() {
		jaSmartyConnect.clearScreen();
		return "{}";
	}

	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public String open() {
		return "{\"open\":\"" + jaSmartyConnect.open() + "\"}";
	}

	@GET
	@Path("/close")
	@Produces(MediaType.APPLICATION_JSON)
	public String close() {
		return "{\"close\":\"" + jaSmartyConnect.close() + "\"}";
	}

	@GET
	@Path("/write/{s}")
	@Produces(MediaType.APPLICATION_JSON)
	public String close(@PathParam("s") String s) {
		jaSmartySend.sendString(s);
		return "{}";
	}

	@GET
	@Path("/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public String refreshDisplay() {
		jaSmartyConnect.refreshDisplay();
		return "{}";
	}

	@GET
	@Path("/cacheLine/{x}/{y}/{str}")
	@Produces(MediaType.APPLICATION_JSON)
	public String cacheWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		jaSmartySend.writeLineToCache(x, y, s);
		return "{}";
	}

	@GET
	@Path("/cache/{x}/{y}/{c}")
	@Produces(MediaType.APPLICATION_JSON)
	public String cacheWrite(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("c") char c) {
		jaSmartySend.writeToCache(x, y, c);
		return "{}";
	}

}
