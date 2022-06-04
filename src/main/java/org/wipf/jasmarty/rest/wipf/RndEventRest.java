package org.wipf.jasmarty.rest.wipf;

import java.sql.SQLException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.TAppRndEvents;

/**
 * @author wipf
 *
 */
@Path("rndevent")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RndEventRest {

	@Inject
	TAppRndEvents rndEvent;

	@POST
	@Path("save")
	public Response save(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + rndEvent.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		rndEvent.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getall() throws SQLException {
		return Response.ok(rndEvent.getAllAsJson().toString()).build();
	}

}
