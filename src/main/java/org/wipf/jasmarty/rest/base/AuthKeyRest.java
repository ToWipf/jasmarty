package org.wipf.jasmarty.rest.base;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.base.AuthKey;
import org.wipf.jasmarty.logic.base.AuthKeyService;

/**
 * @author wipf
 *
 */
@Path("authkey")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class AuthKeyRest {

	@Inject
	AuthKeyService authKeyService;

	@GET
	@Path("getAll")
	public Response getAll() {
		return Response.ok(authKeyService.getAll()).build();
	}

	@POST
	@Path("createOrUpdate")
	public Response createOrUpdate(AuthKey o) {
		authKeyService.save(o);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{username}")
	public Response delete(@PathParam("username") Integer nId) {
		authKeyService.del(nId);
		return Response.ok().build();
	}

}
