package org.wipf.jasmarty.rest.liste;

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

import org.wipf.jasmarty.databasetypes.liste.ListeType;
import org.wipf.jasmarty.logic.listen.ListeTypeService;

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
