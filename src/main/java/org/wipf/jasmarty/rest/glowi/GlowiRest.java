package org.wipf.jasmarty.rest.glowi;

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

	@POST
	@PermitAll
	@Path("setFull")
	@Produces(MediaType.TEXT_PLAIN)
	public void setfull(GlowiData[][] gd) {
		glowi.setFullScreen(gd);
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
