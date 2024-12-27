package org.wipf.jasmarty.rest.checkliste;

import org.wipf.jasmarty.databasetypes.checkliste.CheckListeItem;
import org.wipf.jasmarty.databasetypes.checkliste.CheckListeType;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.checkliste.CheckListeItemService;

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
 * @author wipf
 *
 */
@Path("checkliste/item")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CheckListeItemRest {

	@Inject
	CheckListeItemService clservice;
	@Inject
	AuthKeyService aks;

	@POST
	@Path("save")
	public Response save(CheckListeItem l, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			clservice.save(l);
			return Response.ok().build();
		}
		return null;
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			clservice.del(nId);
			return Response.ok().build();
		}
		return null;
	}

	@GET
	@Path("getAll")
	public Response getAll(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(clservice.getAll()).build();
		}
		return null;
	}

	@GET
	@POST
	@Path("getAllByType")
	public Response getAllByType(CheckListeType ct, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(clservice.getAllByType(ct)).build();
		}
		return null;
	}

}
