package org.wipf.jasmarty.rest.daylog;

import org.wipf.jasmarty.databasetypes.daylog.DaylogEvent;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.daylog.DaylogEventService;
import org.wipf.jasmarty.logic.daylog.DaylogHome;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author Wipf
 *
 */
@Path("daylog/event")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class DaylogEventRest {

	@Inject
	DaylogEventService daylogEventDB;
	@Inject
	DaylogHome daylogHome;
	@Inject
	AuthKeyService aks;

	@GET
	@Path("get/{dateid}")
	public Response get(@PathParam("dateid") Integer nDateid, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogEventDB.getByDateId(nDateid)).build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getAll")
	public Response getall(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogEventDB.getAll()).build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getTextBySearchAndType/{search}/{type}")
	public Response getByDateQuery(@PathParam("search") String sSearch, @PathParam("type") String sType, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogEventDB.getTextBySearchAndType(sSearch, sType)).build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getLastByType/{type}/{anzahl}")
	public Response getLastByType(@PathParam("type") String sType, @PathParam("anzahl") Integer nAnzahl, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogEventDB.getLastByTypeId(sType, nAnzahl)).build();
		}
		return Response.status(471).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogEvent d, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			daylogEventDB.save(d);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			daylogEventDB.del(nId);
			return Response.ok().build();
		}
		return Response.status(471).build();
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
	public Response getAllById(@PathParam("ids") String nIds, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogHome.getAllByTypIdAsJson(nIds)).build();
		}
		return Response.status(471).build();
	}

	/**
	 * Wie oft was
	 * 
	 * @param sTypes
	 * @return
	 */
	@GET
	@Path("getStats/{types}")
	public Response getStats(@PathParam("types") String sTypes, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogEventDB.getStats(sTypes)).build();
		}
		return Response.status(471).build();
	}

}
