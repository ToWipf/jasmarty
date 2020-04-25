package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.RefreshLoop;

/**
 * @author wipf
 *
 */
@Path("/refresh")
@ApplicationScoped
public class RefreshRest {

	@Inject
	RefreshLoop refreshLoop;

	@GET
	@Path("/on")
	@Produces(MediaType.TEXT_PLAIN)
	public Response on() {
		refreshLoop.start();
		return Response.ok("{}").build();
	}

	@GET
	@Path("/off")
	@Produces(MediaType.TEXT_PLAIN)
	public Response off() {
		refreshLoop.stop();
		return Response.ok("{}").build();
	}

	@GET
	@Path("/refreshNow")
	@Produces(MediaType.TEXT_PLAIN)
	public Response refreshNow() {
		refreshLoop.doRefreshLcd();
		return Response.ok("{}").build();
	}

}
