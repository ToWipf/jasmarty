package org.wipf.jasmarty.rest.liste;

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

import org.wipf.jasmarty.databasetypes.liste.ListeType;
import org.wipf.jasmarty.logic.liste.ListeTypeService;

/**
 * @author wipf
 *
 */
@Path("listeType")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ListeTypeRest {

	@Inject
	ListeTypeService listeTypeDB;

	@POST
	@Path("save")
	public Response save(ListeType l) {
		listeTypeDB.save(l);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		listeTypeDB.del(nId);
		return Response.ok("{}").build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(listeTypeDB.getAll()).build();
	}

}
