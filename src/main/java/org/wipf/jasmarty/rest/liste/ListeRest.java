package org.wipf.jasmarty.rest.liste;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.liste.Liste;
import org.wipf.jasmarty.logic.listen.ListeService;

/**
 * @author wipf
 *
 */
@Path("liste")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
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
