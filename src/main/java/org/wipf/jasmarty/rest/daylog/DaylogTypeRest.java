package org.wipf.jasmarty.rest.daylog;

import org.wipf.jasmarty.databasetypes.daylog.DaylogType;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.daylog.DaylogTypeService;

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
@Path("daylog/type")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class DaylogTypeRest {

	@Inject
	DaylogTypeService daylogTypeDB;
	@Inject
	AuthKeyService aks;

	// Wird nicht gebraucht
	@GET
	@Path("get/{id}")
	public Response get(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogTypeDB.get(nId).toString()).build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getAll")
	public Response getall(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(daylogTypeDB.getAll()).build();
		}
		return Response.status(471).build();
	}

	@POST
	@Path("save")
	public Response save(DaylogType d, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			daylogTypeDB.save(d);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			daylogTypeDB.del(nId);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

}
