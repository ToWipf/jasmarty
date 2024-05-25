package org.wipf.jasmarty.rest.telegram;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.TeleLogService;

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
