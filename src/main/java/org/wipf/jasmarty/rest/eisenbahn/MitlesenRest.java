package org.wipf.jasmarty.rest.eisenbahn;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
