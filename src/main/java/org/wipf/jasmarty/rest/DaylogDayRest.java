package org.wipf.jasmarty.rest;

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

import org.wipf.jasmarty.logic.daylog.DaylogDayDB;

/**
 * @author Wipf
 *
 */
@Path("daylog/day")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DaylogDayRest {

	@Inject
	DaylogDayDB daylogDayDB;

	@GET
	@Path("get/{date}/{userid}")
	public Response get(@PathParam("date") String sDate, @PathParam("userid") Integer nUserid) throws SQLException {
		return Response.ok(daylogDayDB.get(sDate, nUserid)).build();
	}

	@GET
	@Path("getAll/{userid}")
	public Response getall(@PathParam("userid") Integer nUserid) throws SQLException {
		return Response.ok(daylogDayDB.getAllAsJson(nUserid).toString()).build();
	}

	@POST
	@Path("save")
	public Response save(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogDayDB.save(jnRoot) + "\"}").build();
	}

	@POST
	@Path("getDateAndCrateIfDateNotExistsByJSON")
	public Response getDateAndCrateIfDateNotExistsByJSON(String jnRoot) throws SQLException {
		return Response.ok(daylogDayDB.getDateAndCrateIfDateNotExistsByJSON(jnRoot)).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogDayDB.del(nId);
		return Response.ok().build();
	}

}
