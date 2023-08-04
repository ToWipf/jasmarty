package org.wipf.jasmarty.rest.mttt;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.logic.mttt.MtttCache;
import org.wipf.jasmarty.logic.mttt.MtttLogic;
import org.wipf.jasmarty.logic.mttt.MtttService;

/**
 * @author Wipf
 *
 */
@Path("mttt")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class MtttRest {

	@Inject
	MtttService mttt;
	@Inject
	MtttCache cache;
	@Inject
	MtttLogic logic;

	@GET
	@Path("full")
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public Response doMttt() {
		return Response.ok(mttt.getFullScreen()).build();
	}

	@GET
	@Path("doClick/{x}/{y}") // VON WEB
	public Response doClick(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		mttt.doSet(x, y);
		return Response.ok().build();
	}

	@GET
	@Path("do/{x}/{y}") // VonArduino
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public Response doArdClick(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		mttt.doSet(x, y);
		return Response.ok(mttt.getFullScreen()).build(); // TODO nur die Änderungen senden
	}

	@GET
	@Path("doId/{id}") // Von Arduino DEBUG
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public Response doArdClick(@PathParam("id") Integer id) {
		mttt.doSetById(id);
		return Response.ok(mttt.getFullScreen()).build(); // TODO nur die Änderungen senden
	}

	@GET
	@Path("startMttt")
	public Response doMtttTest() {
		logic.loadNewGame();
		return Response.ok().build();
	}

	@GET
	@Path("getCache")
	@Produces(MediaType.APPLICATION_JSON)
	public mtttData[][] getCache() {
		return cache.getCache();
	}

	@GET
	@Path("stopApp")
	public void stopApp() {
		mttt.stopApp();
	}

	@GET
	@PermitAll
	@Path("cls")
	public void cls() {
		mttt.cls();
	}
}
