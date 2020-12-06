package org.wipf.jasmarty.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
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
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TelegramRefreshRest {

	@Inject
	RefreshLoop refreshLoop;

	// TODO mv nach lcdRest

	@GET
	@Path("/on")
	public Response on() {
		refreshLoop.start();
		return Response.ok().build();
	}

	@GET
	@Path("/off")
	public Response off() {
		refreshLoop.stop();
		return Response.ok().build();
	}

	@GET
	@Path("/refreshCache")
	public Response refreshCache() {
		refreshLoop.doRefreshCacheManuell();
		return Response.ok().build();
	}

}
