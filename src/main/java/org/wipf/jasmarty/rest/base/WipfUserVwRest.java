package org.wipf.jasmarty.rest.base;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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
@RequestScoped
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
