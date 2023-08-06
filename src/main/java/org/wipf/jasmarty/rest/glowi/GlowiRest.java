package org.wipf.jasmarty.rest.glowi;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.jasmarty.datatypes.glowi.GlowiData;
import org.wipf.jasmarty.logic.glowi.GlowiCache;
import org.wipf.jasmarty.logic.glowi.GlowiService;

/**
 * @author Wipf
 *
 */
@Path("glowi")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class GlowiRest {

	@Inject
	GlowiService glowi;
	@Inject
	GlowiCache cache;

	@GET
	@PermitAll
	@Path("full")
	@Produces(MediaType.TEXT_PLAIN)
	public String getfull() {
		return glowi.getFullScreen();
	}

	@GET
	@PermitAll
	@Path("do/{x}/{y}") // Von Arduino LASER
	@Produces(MediaType.TEXT_PLAIN)
	public String doArdClick(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		glowi.doSet(x, y);
		return glowi.getDivScreen(); // TODO nur die Änderungen senden
	}

	@GET
	@PermitAll
	@Path("doId/{id}") // Von Arduino POTI
	@Produces(MediaType.TEXT_PLAIN)
	public String doArdClick(@PathParam("id") Integer id) {
		glowi.doSetById(id);
		return glowi.getDivScreen(); // TODO nur die Änderungen senden
	}

	@GET
	@PermitAll
	@Path("cls")
	@Produces(MediaType.TEXT_PLAIN)
	public String cls() {
		glowi.cls();
		return getfull();
	}

	@GET
	@PermitAll
	@Path("start")
	public String start() {
		return glowi.startApp();
	}

	/////////////////

	@GET
	@Path("doClick/{x}/{y}") // VON WEB
	public void doClick(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		glowi.doSet(x, y);
	}

	@GET
	@Path("getCache") // Für WEB
	@Produces(MediaType.APPLICATION_JSON)
	public GlowiData[][] getCache() {
		return cache.getCache();
	}

}
