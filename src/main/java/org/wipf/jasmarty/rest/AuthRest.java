package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.Auth;

/**
 * @author wipf
 *
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON) TODO POST geht nicht
//@UserDefinition
//https://quarkus.io/guides/datasource#other-databases
@ApplicationScoped
public class AuthRest {

	@Inject
	Auth auth;

	@POST
	@Path("/add")
	public Response add(String jnRoot) {
		auth.createNew(jnRoot);
		return Response.ok().build();
	}

	@POST
	@Path("/check")
	public Response CurrentPressed(String jnRoot) {
		return Response.ok("{\"auth\":" + auth.check(jnRoot) + "}").build();
	}

}
