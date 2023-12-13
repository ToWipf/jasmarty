package org.wipf.jasmarty.rest.base;

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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.databasetypes.base.WipfUser;
import org.wipf.jasmarty.logic.base.WipfUserVW;

/**
 * @author wipf
 *
 */
@Path("wipfuservw")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfUserVwRest {

	@Inject
	WipfUserVW wipfUserVW;

	@GET
	@Path("getAll")
	public Response getAll() {
		return Response.ok(wipfUserVW.getAll()).build();
	}

	@POST
	@Path("createOrUpdate")
	public Response createOrUpdate(WipfUser wu) throws WipfException {
		wipfUserVW.addOrUpdateUser(wu);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{username}")
	public Response delete(@PathParam("username") String sUsername) {
		wipfUserVW.deleteUser(sUsername);
		return Response.ok().build();
	}

	@GET
	@PermitAll
	@Path("/getUsername")
	@Produces(MediaType.TEXT_PLAIN)
	public Response me(@Context SecurityContext securityContext) {
		return Response.ok(securityContext.getUserPrincipal().getName()).build();
	}

}
