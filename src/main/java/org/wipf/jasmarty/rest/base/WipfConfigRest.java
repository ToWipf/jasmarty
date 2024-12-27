package org.wipf.jasmarty.rest.base;

import org.wipf.jasmarty.databasetypes.base.WipfConfig;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
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
 * @author wipf
 *
 */
@Path("basesettings")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class WipfConfigRest {

	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	AuthKeyService aks;

	@GET
	@PermitAll
	@Path("get/{appname}")
	public Response getConfig(@PathParam("appname") String sAppname, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok("{\"active\":" + wipfConfig.isAppActive(sAppname) + "}").build();
		}
		return null;
	}

	@GET
	@Path("getAll")
	public Response getConfig(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {

			return Response.ok(wipfConfig.getAll()).build();
		}
		return null;
	}

	@POST
	@Path("save")
	public Response saveItem(WipfConfig wu, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			wipfConfig.saveItem(wu);
			return Response.ok("{}").build();
		}
		return null;
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") String sKey, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			wipfConfig.deleteItem(sKey);
			return Response.ok().build();
		}
		return null;
	}

}
