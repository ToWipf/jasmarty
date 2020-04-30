package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.App;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("/wipf")
@ApplicationScoped
public class WipfRest {

	@Inject
	SerialConfig serialConfig;
	@Inject
	App app;

	@GET
	@Path("/ver")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getver() {
		return Response.ok("{\"ver\":\"" + App.VERSION + "\"}").build();
	}

	@GET
	@Path("/ports")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ports() {
		return Response.ok(serialConfig.getPorts().toString()).build();
	}

	@GET
	@Path("/stop")
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopAll() {
		app.stopApp();
		return Response.ok().build();
	}

}
