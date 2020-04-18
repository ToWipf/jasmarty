package org.wipf.jasmarty.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.ActionVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/actions")
public class ActionRest {

	@Inject
	ActionVerwaltung actionVerwaltung;

	@GET
	@Path("/currentPressed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response CurrentPressed() {
		return Response.ok("{\"btn\":" + actionVerwaltung.getCurrentPressed() + "}").build();
	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAction(@PathParam("id") Integer nId) {
		return actionVerwaltung.getActionFromDbByButton(nId).toJson();
	}

	@GET
	@Path("/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll() {
		return actionVerwaltung.getAllFromDBAsJson();
	}

	@POST
	@Path("/set")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setAction(String jnRoot) {
		actionVerwaltung.setAction(jnRoot);
		return Response.ok("{\"save\":\"TODO\"}").build();

	}

	@OPTIONS
	@Path("/set")
	public Response setOptions() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
	}

}
