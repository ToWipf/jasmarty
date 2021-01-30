package org.wipf.jasmarty.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("wipf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class WipfRest {

	@Inject
	SerialConfig serialConfig;
	@Inject
	MainHome qMain;

	@GET
	@Path("ver")
	public Response getver() {
		return Response.ok("{\"ver\":\"" + MainHome.VERSION + "\"}").build();
	}

	@GET
	@Path("ports")
	public Response ports() {
		return Response.ok(serialConfig.getPorts().toString()).build();
	}

	@POST
	@Path("stop")
	public Response stopAll() {
		MainHome.stopApp();
		return Response.ok().build();
	}

}
