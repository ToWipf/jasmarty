package org.wipf.jasmarty.rest.daylog;

import java.util.List;

import org.wipf.jasmarty.databasetypes.daylog.DaylogDay;
import org.wipf.jasmarty.logic.daylog.DaylogDayService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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

	@GET
	@Path("get/{date}")
	public DaylogDay get(@PathParam("date") String sDate) {
		return daylogDayDB.getByDateString(sDate);
	}

	@GET
	@Path("getAllByDateQuery/{dateQuery}")
	public List<DaylogDay> getByDateQuery(@PathParam("dateQuery") String sDateQuery) {
		return daylogDayDB.getAllByDateQuery(sDateQuery);
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
