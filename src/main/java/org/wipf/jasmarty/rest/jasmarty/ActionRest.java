package org.wipf.jasmarty.rest.jasmarty;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.lcd.ButtonAction;
import org.wipf.jasmarty.logic.lcd.ActionVerwaltung;

/**
 * @author wipf
 *
 */
@Path("actions")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
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
