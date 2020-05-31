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

import org.wipf.jasmarty.logic.telegram.TelegramHome;
import org.wipf.jasmarty.logic.telegram.TelegramVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TelegramRest {

	@Inject
	TelegramVerwaltung telegramVerwaltung;
	@Inject
	TelegramHome tHome;

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

	@GET
	@POST
	@Path("/setbot/{bot}")
	public Response setbot(@PathParam("bot") String sBot) {
		return Response.ok("{\"state\":\"" + telegramVerwaltung.setbot(sBot) + "\"}").build();
	}

	@POST
	@Path("/sendMsgToGroup/{msg}")
	public Response sendMsgToGroup(@PathParam("msg") String sMsg) {
		return Response.ok("{\"state\":\"" + telegramVerwaltung.sendMsgToGroup(sMsg) + "\"}").build();
	}

	@GET
	@Path("/telelog")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelog() {
		return Response.ok(telegramVerwaltung.getTelegramLog(null)).build();
	}

	@GET
	@Path("/telelogtf")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelogtf() {
		// TODO id form db
		return Response.ok(telegramVerwaltung.getTelegramLog("798200105")).build();
	}

}
