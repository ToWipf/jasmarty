package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@Path("/crypt")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
@ApplicationScoped
public class CryptRest {

	@Inject
	Wipf wipf;

	@POST
	@GET
	@Path("/encrypt/{key}/{str}")
	public Response encrypt(@PathParam("str") String sStr, @PathParam("key") String sKey) {
		return Response.ok(wipf.encrypt(sStr, sKey)).build();
	}

	@POST
	@GET
	@Path("/decrypt/{key}/{str}")
	public Response decrypt(@PathParam("str") String sStr, @PathParam("key") String sKey) {
		return Response.ok(wipf.decrypt(sStr, sKey)).build();
	}

	@POST
	@Path("/encrypt")
	public Response encryptJson(String jnRoot) {
		return Response.ok(wipf.encrypt(jnRoot)).build();
	}

	@POST
	@Path("/decrypt")
	public Response decryptJson(String jnRoot) {
		return Response.ok(wipf.decrypt(jnRoot)).build();
	}

}
