package org.wipf.jasmarty.rest.debug;

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

import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@Path("crypt")
@RolesAllowed("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CryptRest {

	@Inject
	Wipf wipf;

	@POST
	@GET
	@Path("encrypt/{key}/{str}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response encrypt(@PathParam("str") String sStr, @PathParam("key") String sKey) {
		return Response.ok(wipf.encrypt(sStr, sKey)).build();
	}

	@POST
	@GET
	@Path("decrypt/{key}/{str}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response decrypt(@PathParam("str") String sStr, @PathParam("key") String sKey) {
		return Response.ok(wipf.decrypt(sStr, sKey)).build();
	}

	@POST
	@Path("encrypt")
	public Response encryptJson(String jnRoot) {
		return Response.ok("{\"data\":\"" + wipf.encrypt(jnRoot) + "\"}").build();
	}

	@POST
	@Path("decrypt")
	public Response decryptJson(String jnRoot) {
		return Response.ok("{\"data\":\"" + wipf.decrypt(jnRoot).replaceAll("\n", "\\\\n") + "\"}").build();
	}

}
