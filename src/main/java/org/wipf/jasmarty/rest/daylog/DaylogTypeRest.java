package org.wipf.jasmarty.rest.daylog;

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

import org.wipf.jasmarty.databasetypes.daylog.DaylogType;
import org.wipf.jasmarty.logic.daylog.DaylogTypeService;

/**
 * @author Wipf
 *
 */
@Path("daylog/type")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DaylogTypeRest {

	@Inject
	DaylogTypeService daylogTypeDB;

	// Wird nicht gebraucht
	@GET
	@Path("get/{id}")
	public Response get(@PathParam("id") Integer nId) {
		return Response.ok(daylogTypeDB.get(nId).toString()).build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(daylogTypeDB.getAll()).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogType d) {
		daylogTypeDB.save(d);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		daylogTypeDB.del(nId);
		return Response.ok("{}").build();
	}

}
