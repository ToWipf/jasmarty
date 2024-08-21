package org.wipf.jasmarty.rest.checkliste;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeListe;
import org.wipf.jasmarty.logic.checkliste.CheckListeListeService;

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
@Path("checkliste/liste")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CheckListeListeRest {

	@Inject
	CheckListeListeService clservice;

	@POST
	@Path("save")
	public Response save(CheckListeListe l) {
		clservice.save(l);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		clservice.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getAll() {
		return Response.ok(clservice.getAll()).build();
	}

}
