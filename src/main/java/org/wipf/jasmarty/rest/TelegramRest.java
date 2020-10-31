package org.wipf.jasmarty.rest;

import java.sql.SQLException;

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
import org.wipf.jasmarty.logic.telegram.TAppMsg;
import org.wipf.jasmarty.logic.telegram.TeleLog;
import org.wipf.jasmarty.logic.telegram.TeleMenue;
import org.wipf.jasmarty.logic.telegram.TelegramHome;

/**
 * @author wipf
 *
 */
@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TelegramRest {

	@Inject
	SendAndReceive tVerwaltung;
	@Inject
	TelegramHome tHome;
	@Inject
	TeleLog tlog;
	@Inject
	TeleMenue tMenue;
	@Inject
	TAppMsg tAppMsg;

	@GET
	@Path("/on")
	public Response on() {
		try {
			tHome.telegramStart();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try {
			return Response.ok("{\"status\":\"" + tVerwaltung.setbot(sBot) + "\"}").build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/getbot")
	public Response getbot() {
		return Response.ok("{\"botkey\":\"" + tVerwaltung.getBotKey() + "\"}").build();
	}

	@POST
	@Path("/sendMsgToGroup/{msg}")
	public Response sendMsgToGroup(@PathParam("msg") String sMsg) {
		tVerwaltung.sendMsgToGroup(sMsg);
		// TODO:
		return Response.ok("{}").build();
	}

	@POST
	@Path("/sendMsgToAdmin/{msg}")
	public Response sendMsgToId(@PathParam("msg") String sMsg) {
		tVerwaltung.sendMsgToAdmin(sMsg);
		// TODO:
		return Response.ok("{}").build();
	}

	@GET
	@Path("/telelog")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelog() {
		return Response.ok(tlog.genTelegramLog(null)).build();
	}

	@GET
	@Path("/telelogtf_old")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelogtfold() {
		// TODO id form db
		return Response.ok(tlog.genTelegramLog("798200105")).build();
	}

	@GET
	@Path("/telelogtf")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelogtf() {
		// TODO id form db
		return Response.ok(tlog.genTelegramLogUnknown()).build();
	}

	@GET
	@Path("/log")
	public Response log() {
		return Response.ok(tlog.getTelegramLogAsJson().toString()).build();
	}

	@GET
	@Path("/logext")
	public Response logs() {
		return Response.ok(tlog.getTelegramLogAsJsonEXTERN().toString()).build();
	}

	@POST
	@Path("/chat")
	public Response chat(String sJson) {
		return Response.ok("{\"msg\":\"" + tMenue.menueMsg(sJson).replaceAll("\n", "\\\\n") + "\"}").build();
	}

	@GET
	@Path("/msgall")
	public Response msg() {
		return Response.ok(tAppMsg.getMsgAsJson().toString()).build();
	}

}
