package org.wipf.jasmarty.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.WipfUserVW;

/**
 * @author wipf
 *
 */
@Path("/wipfuservw")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfUserVwRest {

	@Inject
	WipfUserVW wipfUserVW;

	@GET
	@Path("/getAll")
	public Response getAll() {
		return Response.ok(wipfUserVW.getAllUsersAsJson(false).toString()).build();
	}

	@POST
	@Path("/createOrUpdate")
	public Response createOrUpdate(String sJson) {
		wipfUserVW.addOrUpdateUser(sJson);
		return Response.ok().build();
	}

}
