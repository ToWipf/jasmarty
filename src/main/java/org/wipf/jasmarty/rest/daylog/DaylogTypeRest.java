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

import org.wipf.jasmarty.logic.daylog.DaylogTypeDB;

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
	DaylogTypeDB daylogTypeDB;

	@GET
	@Path("get/{dateid}")
	public Response get(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogTypeDB.getAsJson(nDateid).toString()).build();
	}

	@GET
	@Path("getAll")
	public Response getall() throws SQLException {
		return Response.ok(daylogTypeDB.getAllAsJson().toString()).build();
	}

	@POST
	@Path("save")
	public Response save(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogTypeDB.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogTypeDB.del(nId);
		return Response.ok().build();
	}

}
