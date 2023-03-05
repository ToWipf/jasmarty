package org.wipf.jasmarty.rest.telegram;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.telegram.TeleMsg;
import org.wipf.jasmarty.logic.telegram.TAppMsgService;

/**
 * @author wipf
 *
 */
@Path("telemsg")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
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
