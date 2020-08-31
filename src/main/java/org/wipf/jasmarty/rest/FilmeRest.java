package org.wipf.jasmarty.rest;

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

import org.wipf.jasmarty.logic.telegram.TAppFilme;

/**
 * @author wipf
 *
 */
@Path("/filme")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FilmeRest {

	@Inject
	TAppFilme appFilme;

	@POST
	@Path("/save")
	public Response save(String jnRoot) {
		return Response.ok("{\"save\":\"" + appFilme.saveItem(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("/delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		appFilme.deleteItem(nId);
		return Response.ok().build();
	}

	@GET
	@Path("/getAll")
	public Response getall() {
		return Response.ok(appFilme.getAllAsJson().toString()).build();
	}

}
