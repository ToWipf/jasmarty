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

import org.wipf.jasmarty.databasetypes.liste.Liste;
import org.wipf.jasmarty.logic.listen.ListeService;

/**
 * @author wipf
 *
 */
@Path("liste")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ListeRest {

	@Inject
	ListeService listeDB;

	@POST
	@Path("save")
	public Response save(Liste l) {
		listeDB.save(l);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		listeDB.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getAll() {
		return Response.ok(listeDB.getAll()).build();
	}

	@GET
	@Path("getAllByType/{typeid}")
	public Response getAllByType(@PathParam("typeid") Integer nTypeId) {
		return Response.ok(listeDB.getAllByType(nTypeId)).build();
	}

}
