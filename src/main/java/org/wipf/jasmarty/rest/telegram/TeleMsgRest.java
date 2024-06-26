package org.wipf.jasmarty.rest.telegram;

import org.wipf.jasmarty.databasetypes.telegram.TeleMsg;
import org.wipf.jasmarty.logic.telegram.TAppMsgService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
@Path("telemsg")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class TeleMsgRest {

	@Inject
	TAppMsgService tAppMsg;

	@GET
	@Path("getall")
	public Response msg() {
		return Response.ok(tAppMsg.getAll()).build();
	}

	@POST
	@Path("save")
	public Response saveMsg(TeleMsg t) {
		tAppMsg.save(t);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delMsg(@PathParam("id") Integer nId) {
		tAppMsg.del(nId);
		return Response.ok("{}").build();
	}

}
