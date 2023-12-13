package org.wipf.jasmarty.rest.telegram;

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

import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;
import org.wipf.jasmarty.logic.telegram.TeleMenue;
import org.wipf.jasmarty.logic.telegram.TelegramHome;

/**
 * @author wipf
 *
 */
@Path("telegram")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TelegramRest {

	@Inject
	TSendAndReceive tVerwaltung;
	@Inject
	TelegramHome tHome;
	@Inject
	TeleMenue tMenue;

	@GET
	@Path("on")
	public Response on() {
		tHome.telegramStart();
		return Response.ok().build();
	}

	@GET
	@Path("off")
	public Response off() {
		tHome.telegramStop();
		return Response.ok().build();
	}

	@POST
	@Path("setbot/{bot}")
	public Response setbot(@PathParam("bot") String sBot) throws WipfException {
		return Response.ok("{\"status\":\"" + tVerwaltung.setbot(sBot) + "\"}").build();
	}

	@GET
	@Path("getbot")
	public Response getbot() throws WipfException {
		return Response.ok("{\"botkey\":\"" + tVerwaltung.getBotKeyFromCache() + "\"}").build();
	}

	@POST
	@PermitAll
	@Path("sendMsgTo/{gid}/{msg}")
	public Response sendMsgTo(@PathParam("gid") Long nGid, @PathParam("msg") String sMsg) {
		tVerwaltung.sendMsgTo(nGid, sMsg);
		return Response.ok("{}").build();
	}

	@POST
	@PermitAll
	@Path("sendMsgToAdmin")
	public Response sendMsgToId(String sMsg) {
		tVerwaltung.sendMsgToAdmin(sMsg);
		return Response.ok("{}").build();
	}

	@POST
	@Path("chat")
	public Response chat(String sJson) {
		return Response.ok("{\"msg\":\"" + tMenue.menueMsgApi(sJson) + "\"}").build();
	}

}
