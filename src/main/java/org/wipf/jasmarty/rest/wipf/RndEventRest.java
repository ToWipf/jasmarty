package org.wipf.jasmarty.rest.wipf;

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

import org.wipf.jasmarty.databasetypes.liste.RndEvent;
import org.wipf.jasmarty.logic.liste.RndEventsService;

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
