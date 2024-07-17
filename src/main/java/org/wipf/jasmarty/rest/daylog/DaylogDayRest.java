package org.wipf.jasmarty.rest.daylog;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.daylog.DaylogDayService;

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
@Path("daylog/day")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class DaylogDayRest {

	@Inject
	DaylogDayService daylogDayDB;
	@Inject
	AuthKeyService aks;

	@GET
	@Path("get/{date}")
	public Response get(@PathParam("date") String sDate, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogDayDB.get(sDate)).build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getAllByDateQuery/{dateQuery}")
	public Response getByDateQuery(@PathParam("dateQuery") String sDateQuery, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogDayDB.getAllByDateQuery(sDateQuery)).build();
		}
		return Response.status(471).build();
	}

//	@GET
//	@Path("getAll")
//	public Response getall(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
//		if (aks.isKeyInCache(key)) {
//			return Response.ok(daylogDayDB.getAll()).build();
//		}
//		return Response.status(471).build();
//	}

	@POST
	@Path("save")
	public Response save(DaylogDay d, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogDayDB.save(d)).build();
		}
		return Response.status(471).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			daylogDayDB.del(nId);
			return Response.ok("{}").build();
		}
		return Response.status(471).build();
	}

}
