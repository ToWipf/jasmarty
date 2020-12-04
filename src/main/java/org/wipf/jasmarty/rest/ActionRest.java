package org.wipf.jasmarty.rest;

import java.sql.SQLException;

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

import org.wipf.jasmarty.logic.jasmarty.ActionVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/actions")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON) TODO POST geht nicht
@ApplicationScoped
public class ActionRest {

	@Inject
	ActionVerwaltung actionVerwaltung;

	@GET
	@Path("/currentPressed")
	public Response CurrentPressed() {
		return Response.ok("{\"btn\":" + actionVerwaltung.getCurrentPressed() + "}").build();
	}

	@GET
	@Path("/get/{id}")
	public Response getAction(@PathParam("id") Integer nId) {
		return Response.ok(actionVerwaltung.getActionFromDbByButton(nId).toJson()).build();
	}

	@GET
	@Path("/getall")
	public Response getAll() {
		return Response.ok(actionVerwaltung.getAllFromDBAsJson().toString()).build();
	}

	@GET
	@Path("/doaction/{id}")
	public Response doaction(@PathParam("id") Integer nId) {
		try {
			actionVerwaltung.doActionById(nId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@POST
	@Path("/set")
	public Response setAction(String jnRoot) {
		try {
			actionVerwaltung.setAction(jnRoot);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("/delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		try {
			actionVerwaltung.delete(nId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

}
