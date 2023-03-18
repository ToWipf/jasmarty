package org.wipf.jasmarty.rest.daylog;

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

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.logic.daylog.DaylogEventService;
import org.wipf.jasmarty.logic.daylog.DaylogHome;

/**
 * @author Wipf
 *
 */
@Path("daylog/event")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DaylogEventRest {

	@Inject
	DaylogEventService daylogEventDB;
	@Inject
	DaylogHome daylogHome;

	@GET
	@Path("get/{dateid}")
	public Response get(@PathParam("dateid") Integer nDateid) {
		return Response.ok(daylogEventDB.getByDateId(nDateid)).build();
	}

	@GET
	@Path("getAllById/{ids}")
	public Response getAllById(@PathParam("ids") String nIds) {
		return Response.ok(daylogHome.getAllByTypIdAsJson(nIds)).build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(daylogEventDB.getAll()).build();
	}

	@GET
	@Path("getTextBySearchAndType/{search}/{type}")
	public Response getByDateQuery(@PathParam("search") String sSearch, @PathParam("type") String sType) {
		return Response.ok(daylogEventDB.getTextBySearchAndType(sSearch, sType)).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogEvent d) {
		daylogEventDB.save(d);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		daylogEventDB.del(nId);
		return Response.ok("{}").build();
	}

	@GET
	@Path("getStats/{types}")
	public Response getStats(@PathParam("types") String sTypes) {
		return Response.ok(daylogEventDB.getStats(sTypes).toString()).build();
	}

}
