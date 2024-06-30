package org.wipf.jasmarty.rest.jasmarty;

import org.wipf.jasmarty.logic.lcd.JasmartyHome;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author wipf
 *
 */
@Path("refresh")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
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
