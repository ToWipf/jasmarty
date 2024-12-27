package org.wipf.jasmarty.rest.wipf;

import org.wipf.jasmarty.databasetypes.liste.RndEvent;
import org.wipf.jasmarty.logic.listen.RndEventsService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author wipf
 *
 */
@Path("rndevent")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class RndEventRest {

	@Inject
	RndEventsService rndEvent;

	@POST
	@Path("save")
	public Response save(RndEvent r) {
		rndEvent.save(r);
		return Response.ok().build();
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
