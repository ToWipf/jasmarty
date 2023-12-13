package org.wipf.jasmarty.rest.eisenbahn;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.wipf.jasmarty.logic.eisenbahn.Mitlesen;

@Path("eisenbahn/mitlesen")
//@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
@ApplicationScoped
public class MitlesenRest {

	@Inject
	Mitlesen mitlesen;

	@POST
	@GET
	@Path("start")
	public Response start() {
		mitlesen.doStartMitlesen();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("stop")
	public Response stop() {
		mitlesen.doStopMitlesen();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("connect")
	public Response connect() {
		return Response.ok("{\"ok\":\"" + mitlesen.connect() + "\"}").build();
	}

	@GET
	@Path("list")
	public Response list() {
		return Response.ok(mitlesen.getList().toString()).build();
	}

	@GET
	@POST
	@Path("add/{text}")
	public Response getConfig(@PathParam("text") String sText) {
		mitlesen.addDebugItem(sText);
		return Response.ok().build();
	}

}
