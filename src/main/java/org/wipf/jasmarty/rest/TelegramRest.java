package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.SendAndReceive;
import org.wipf.jasmarty.logic.telegram.TelegramHome;
import org.wipf.jasmarty.logic.telegram.messageEdit.TeleLog;
import org.wipf.jasmarty.logic.telegram.messageEdit.TeleMenue;

/**
 * @author wipf
 *
 */
@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TelegramRest {

	@Inject
	SendAndReceive telegramVerwaltung;
	@Inject
	TelegramHome tHome;
	@Inject
	TeleLog msglog;
	@Inject
	TeleMenue menue;

	@GET
	@Path("/on")
	public Response on() {
		tHome.telegramStart();
		return Response.ok().build();
	}

	@GET
	@Path("/off")
	public Response off() {
		tHome.telegramStop();
		return Response.ok().build();
	}

	@POST
	@Path("/setbot/{bot}")
	public Response setbot(@PathParam("bot") String sBot) {
		return Response.ok("{\"status\":\"" + telegramVerwaltung.setbot(sBot) + "\"}").build();
	}

	@GET
	@Path("/getbot")
	public Response getbot() {
		return Response.ok("{\"botkey\":\"" + telegramVerwaltung.getBotKey() + "\"}").build();
	}

	@POST
	@Path("/sendMsgToGroup/{msg}")
	public Response sendMsgToGroup(@PathParam("msg") String sMsg) {
		telegramVerwaltung.sendMsgToGroup(sMsg);
		// TODO:
		return Response.ok("{\"status\":\"true\"}").build();
	}

	@GET
	@Path("/telelog")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelog() {
		return Response.ok(msglog.genTelegramLog(null)).build();
	}

	@GET
	@Path("/telelogtf")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelogtf() {
		// TODO id form db
		return Response.ok(msglog.genTelegramLog("798200105")).build();
	}

	@GET
	@Path("/log")
	public Response log() {
		return Response.ok(msglog.getTelegramLogAsJson().toString()).build();
	}

	@POST
	@Path("/chat")
	public Response chat(String sJson) {
		return Response.ok("{\"msg\":\"" + menue.menueMsg(sJson).replaceAll("\n", "\\\\n") + "\"}").build();
	}

}
