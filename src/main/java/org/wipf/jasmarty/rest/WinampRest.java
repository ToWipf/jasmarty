package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.actions.Winamp;

/**
 * @author wipf
 *
 */
@Path("/winamp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WinampRest {

	@Inject
	Winamp winamp;

	@GET
	@Path("/control/{s}")
	public Response control(@PathParam("s") String s) {
		winamp.control(s);
		return Response.ok("{}").build();
	}

	@GET
	@Path("/info/{s}")
	public Response info(@PathParam("s") String s) {
		return Response.ok("{\"info\":\"" + winamp.getInfos(s) + "\"}").build();
	}

}
