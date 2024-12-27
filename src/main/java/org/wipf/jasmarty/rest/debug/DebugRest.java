package org.wipf.jasmarty.rest.debug;

import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.lcd.JasmartyHome;
import org.wipf.jasmarty.logic.tasks.CronDaily;
import org.wipf.jasmarty.logic.telegram.TAppGrafana;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author wipf
 *
 */
@Path("debug")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DebugRest {

	@Inject
	JasmartyHome jHome;
	@Inject
	TAppGrafana tAppGrafana;
	@Inject
	Wipf wipf;
	@Inject
	CronDaily cronDaily;

	@GET
	@PUT
	@POST
	@DELETE
	@Path("test")
	public Response test() {
		return Response.ok("{\"test\": \"ok\"}").build();
	}

	@POST
	@GET
	@Path("dailyTask")
	public Response dailyTask() {
		cronDaily.dailyTask();
		return Response.ok().build();
	}

}
