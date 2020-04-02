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
	@Path("/send/{int}")
	@Produces(MediaType.APPLICATION_JSON)
	public String testSerial(@PathParam("int") Integer n) {
		return "{\"test\":\"" + jaSmartyConnect.send(n) + "\"}";
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

}
