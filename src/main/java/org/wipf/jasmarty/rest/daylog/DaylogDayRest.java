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

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;
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
	@Path("get/{date}")
	public Response get(@PathParam("date") String sDate) {
		return Response.ok(daylogDayDB.get(sDate)).build();
	}

	@GET
	@Path("getAllByDateQuery/{dateQuery}")
	public Response getByDateQuery(@PathParam("dateQuery") String sDateQuery) {
		return Response.ok(daylogDayDB.getAllByDateQuery(sDateQuery)).build();
	}

	@GET
	@Path("getAll/{userid}")
	public Response getall(@PathParam("userid") Integer nUserid) {
		return Response.ok(daylogDayDB.getAll()).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogDay d) {
		daylogDayDB.save(d);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogDayDB.del(nId);
		return Response.ok().build();
	}

}
