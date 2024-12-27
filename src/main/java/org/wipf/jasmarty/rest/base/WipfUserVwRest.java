package org.wipf.jasmarty.rest.base;

import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.databasetypes.base.WipfUser;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.WipfUserVW;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
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
	@Inject
	AuthKeyService aks;

	@GET
	@Path("getAll")
	public Response getAll(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(wipfUserVW.getAll()).build();
		}
		return Response.status(471).build();
	}

	@POST
	@Path("createOrUpdate")
	public Response createOrUpdate(WipfUser wu, @CookieParam(MainHome.AUTH_KEY_NAME) String key) throws WipfException {
		if (aks.isKeyInCache(key)) {
			wipfUserVW.addOrUpdateUser(wu);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@DELETE
	@Path("delete/{username}")
	public Response delete(@PathParam("username") String sUsername, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			wipfUserVW.deleteUser(sUsername);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("/getUsername")
	@Produces(MediaType.TEXT_PLAIN)
	public Response me(@Context SecurityContext securityContext) {
		return Response.ok(securityContext.getUserPrincipal().getName()).build();
	}

}
