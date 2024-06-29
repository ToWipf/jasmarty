package org.wipf.jasmarty.rest.jasmarty;

import org.wipf.jasmarty.databasetypes.lcd.ButtonAction;
import org.wipf.jasmarty.logic.lcd.ActionVerwaltung;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
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

/**
 * @author wipf
 *
 */
@Path("actions")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ActionRest {

	@Inject
	ActionVerwaltung actionVerwaltung;

	@GET
	@Path("currentPressed")
	public Response CurrentPressed() {
		return Response.ok("{\"btn\":" + actionVerwaltung.getCurrentPressed() + "}").build();
	}

	@GET
	@Path("get/{id}")
	public Response getAction(@PathParam("id") Integer nId) {
		return Response.ok(actionVerwaltung.getActionByButton(nId)).build();
	}

	@GET
	@Path("getall")
	public Response getAll() {
		return Response.ok(actionVerwaltung.getAll()).build();
	}

	@GET
	@Path("doaction/{id}")
	public Response doaction(@PathParam("id") Integer nId) {
		actionVerwaltung.doActionById(nId);
		return Response.ok().build();
	}

	@POST
	@Path("save")
	public Response setAction(ButtonAction o) {
		actionVerwaltung.save(o);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		actionVerwaltung.delete(nId);
		return Response.ok().build();
	}

}
