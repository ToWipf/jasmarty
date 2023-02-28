package org.wipf.jasmarty.rest.base;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.base.AuthKey;

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

	// TODO

	@GET
	@Path("do")
	@Transactional
	@Produces(MediaType.TEXT_PLAIN)
	public Response doit() {

		AuthKey b = new AuthKey();
		b.key = "test";

		for (int i = 0; i < 100; i++) {

			AuthKey a = new AuthKey();
			a.key = "42";
			a.persist();
		}

		b.persist();

		return Response.ok(b.toString()).build();
	}

}
