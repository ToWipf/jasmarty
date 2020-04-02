package org.wipf.wipfapp.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.wipfapp.logic.base.App;
import org.wipf.wipfapp.logic.jasmarty.jasmarty;

/**
 * @author wipf
 *
 */
@Path("/lcd")
public class RestJaSmarty {

	@Inject
	jasmarty jasmarty;

	@GET
	@Path("/v")
	@Produces(MediaType.APPLICATION_JSON)
	public String getver() {
		return "{\"ver\":\"" + App.VERSION + "\"}";
	}

	@GET
	@Path("/send/{int}")
	@Produces(MediaType.APPLICATION_JSON)
	public String testSerial(@PathParam("int") Integer n) {
		return "{\"test\":\"" + jasmarty.send(n) + "\"}";
	}

	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public String open() {
		return "{\"open\":\"" + jasmarty.open() + "\"}";
	}

	@GET
	@Path("/close")
	@Produces(MediaType.APPLICATION_JSON)
	public String close() {
		return "{\"close\":\"" + jasmarty.close() + "\"}";
	}
}
