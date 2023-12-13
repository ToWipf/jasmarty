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

	//////////////// STATS

	/**
	 * Was wann
	 * 
	 * @param nIds
	 * @return
	 */
	@GET
	@Path("getAllById/{ids}")
	public Response getAllById(@PathParam("ids") String nIds) {
		return Response.ok(daylogHome.getAllByTypIdAsJson(nIds)).build();
	}

	/**
	 * Wie oft was
	 * 
	 * @param sTypes
	 * @return
	 */
	@GET
	@Path("getStats/{types}")
	public Response getStats(@PathParam("types") String sTypes) {
		return Response.ok(daylogEventDB.getStats(sTypes)).build();
	}

}
