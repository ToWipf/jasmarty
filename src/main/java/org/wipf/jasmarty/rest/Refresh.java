package org.wipf.jasmarty.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.jasmarty.logic.jasmarty.RefreshLoop;

/**
 * @author wipf
 *
 */
@Path("/refresh")
public class Refresh {

	@Inject
	RefreshLoop refreshLoop;

	@GET
	@Path("/on")
	@Produces(MediaType.TEXT_PLAIN)
	public String on() {
		refreshLoop.start();
		return "{}";
	}

	@GET
	@Path("/off")
	@Produces(MediaType.TEXT_PLAIN)
	public String off() {
		refreshLoop.stop();
		return "{}";
	}

}
