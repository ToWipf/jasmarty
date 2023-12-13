package org.wipf.jasmarty.rest.jasmarty;

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

import org.wipf.jasmarty.logic.lcd.JasmartyHome;

/**
 * @author wipf
 *
 */
@Path("refresh")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LcdRefreshRest {

	@Inject
	JasmartyHome jasmartyHome;

	@GET
	@Path("on")
	public Response on() {
		// TODO
		jasmartyHome.jasmartyStart();
		return Response.ok().build();
	}

	@GET
	@Path("off")
	public Response off() {
		jasmartyHome.jasmartyStop();
		return Response.ok().build();
	}

}
