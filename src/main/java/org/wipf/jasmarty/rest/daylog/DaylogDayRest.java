package org.wipf.jasmarty.rest.daylog;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
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
import org.wipf.jasmarty.logic.daylog.DaylogDayService;

/**
 * @author Wipf
 *
 */
@Path("daylog/day")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class DaylogDayRest {

	@Inject
	DaylogDayService daylogDayDB;

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
	public DaylogDay save(DaylogDay d) {
		return daylogDayDB.save(d);
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		daylogDayDB.del(nId);
		return Response.ok("{}").build();
	}

}
