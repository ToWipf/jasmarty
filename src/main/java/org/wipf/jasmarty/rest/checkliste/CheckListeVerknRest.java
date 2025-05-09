package org.wipf.jasmarty.rest.checkliste;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeVerkn;
import org.wipf.jasmarty.logic.checkliste.CheckListeVerknService;

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
@Path("checkliste/verkn")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CheckListeVerknRest {

	@Inject
	CheckListeVerknService clservice;

	@POST
	@Path("save")
	public Response save(CheckListeVerkn l) {
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

	@GET
	@Path("getByClID/{id}")
	public Response getByClID(@PathParam("id") Integer nId) {
		return Response.ok(clservice.getByClID(nId)).build();
	}

	@DELETE
	@Path("reset/{id}")
	public Response reset(@PathParam("id") Integer nId) {
		clservice.resetFullList(nId);
		return Response.ok().build();
	}

}
