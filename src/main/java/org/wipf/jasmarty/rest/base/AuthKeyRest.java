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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

	// @GET
	// @Path("bla")
	// @PermitAll
	// @Produces(MediaType.TEXT_PLAIN)
	// public String get( RoutingContext routingContext) {
	// 	// import io.vertx.ext.web.RoutingContext;
	// 	// import io.vertx.core.http.Cookie;
	// 	//Cookie cookie = io.vertx.ext.web.routingContext.getCookie("cookieName");
    //     // if (cookie != null) {
    //     //     return cookie.getValue();
    //     // } else {
    //     //     return "Cookie not found";
    //     // }
	// 	//return "";
	// }

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

	@POST
	@PermitAll
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("newkey")
	public Response newKey(String sKey) {
		authKeyService.newKey(sKey);
		return Response.ok().build();
	}

}
