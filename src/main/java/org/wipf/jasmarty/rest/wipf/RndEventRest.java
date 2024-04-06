package org.wipf.jasmarty.rest.wipf;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.liste.RndEvent;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.listen.RndEventsService;

/**
 * @author wipf
 *
 */
@Path("rndevent")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RndEventRest {

	@Inject
	RndEventsService rndEvent;
	@Inject
	AuthKeyService aks;

	@POST
	@Path("save")
	public Response save(RndEvent r, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			rndEvent.save(r);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			rndEvent.del(nId);
			return Response.ok().build();
		}
		return Response.status(471).build();
	}

	@GET
	@Path("getAll")
	public Response getall(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(rndEvent.getAll()).build();
		}
		return Response.status(471).build();
	}

}
