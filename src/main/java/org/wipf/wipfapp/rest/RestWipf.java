package org.wipf.wipfapp.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.wipfapp.logic.base.App;
import org.wipf.wipfapp.logic.base.Serial;

/**
 * @author wipf
 *
 */
@Path("/wipf")
public class RestWipf {

	@Inject
	Serial serial;

	@GET
	@Path("/ver")
	@Produces(MediaType.APPLICATION_JSON)
	public String getver() {
		return "{\"ver\":\"" + App.VERSION + "\"}";
	}

	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() throws IOException, InterruptedException {
		return "{\"test\":\"" + serial.test() + "\"}";
	}

}
