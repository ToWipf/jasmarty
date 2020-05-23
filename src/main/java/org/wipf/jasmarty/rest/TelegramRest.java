package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.TelegramVerwaltung;

import com.google.inject.Inject;

/**
 * @author wipf
 *
 */
@ApplicationScoped
@Path("/telegram")
public class TelegramRest {

	@Inject
	TelegramVerwaltung telegramVerwaltung;

	@POST
	@Path("/setbot/{bot}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response setbot(@PathParam("bot") String sBot) {
		return Response.ok(telegramVerwaltung.setbot(sBot)).build();
	}

	@POST
	@Path("/sendMsgToGroup/{msg}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sendMsgToGroup(@PathParam("msg") String sMsg) {
		return Response.ok(telegramVerwaltung.sendMsgToGroup(sMsg)).build();
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
		return Response.ok(telegramVerwaltung.getTelegramLog("798200105")).build();
	}

}
