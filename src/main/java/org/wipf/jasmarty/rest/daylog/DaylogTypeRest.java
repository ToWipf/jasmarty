package org.wipf.jasmarty.rest.daylog;

import java.sql.SQLException;

import javax.annotation.security.RolesAllowed;
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
	public Response get(@PathParam("id") Integer nId) throws SQLException {
		return Response.ok(daylogTypeDB.get(nId).toString()).build();
	}

	@GET
	@Path("getAll")
	public Response getall() throws SQLException {
		return Response.ok(daylogTypeDB.getAll()).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogType d) throws SQLException {
		daylogTypeDB.save(d);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogTypeDB.del(nId);
		return Response.ok("{}").build();
	}

}
