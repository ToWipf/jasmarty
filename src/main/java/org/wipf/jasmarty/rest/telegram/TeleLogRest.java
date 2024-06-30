package org.wipf.jasmarty.rest.telegram;

import org.wipf.jasmarty.logic.telegram.TeleLogService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author wipf
 *
 */
@Path("telelog")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class TeleLogRest {

	@Inject
	TeleLogService tlog;

	@GET
	@Path("getall")
	public Response logs() {
		return Response.ok(tlog.getAll()).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delLog(@PathParam("id") Integer nId) {
		tlog.delItem(nId);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("cleanLog")
	public Response cleanLog() {
		tlog.cleanLog();
		return Response.ok().build();
	}

}
