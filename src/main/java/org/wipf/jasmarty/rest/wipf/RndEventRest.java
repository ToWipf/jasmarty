package org.wipf.jasmarty.rest.wipf;

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

import org.wipf.jasmarty.databasetypes.liste.RndEvent;
import org.wipf.jasmarty.logic.listen.RndEventsService;

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
	RndEventsService rndEvent;

	@POST
	@Path("save")
	public Response save(RndEvent r) {
		rndEvent.save(r);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		rndEvent.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(rndEvent.getAll()).build();
	}

}
