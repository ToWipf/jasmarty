package org.wipf.jasmarty.rest.telegram;

import javax.annotation.security.PermitAll;
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

import org.wipf.jasmarty.WipfException;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;
import org.wipf.jasmarty.logic.telegram.TeleLogService;
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
	TeleLogService tlog;
	@Inject
	TeleMenue tMenue;

	@GET
	@Path("on")
	public Response on() {
		try {
			tHome.telegramStart();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public Response setbot(@PathParam("bot") String sBot) {
		try {
			return Response.ok("{\"status\":\"" + tVerwaltung.setbot(sBot) + "\"}").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("getbot")
	public Response getbot() {
		try {
			return Response.ok("{\"botkey\":\"" + tVerwaltung.getBotKeyFromCache() + "\"}").build();
		} catch (WipfException e) {
			System.out.println("getbot:" + e);
			return null;
		}
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
	@Path("sendMsgToAdmin/{msg}")
	public Response sendMsgToId(@PathParam("msg") String sMsg) {
		tVerwaltung.sendMsgToAdmin(sMsg);
		return Response.ok("{}").build();
	}

	@GET
	@Path("logext")
	public Response logs() {
		return Response.ok(tlog.getAll()).build();
	}

	@DELETE
	@Path("delLog/{id}")
	public Response delLog(@PathParam("id") Long nId) {
		tlog.delItem(nId);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("cleanLog")
	public Response cleanLog() {
		tlog.cleanLog();
		return Response.ok().build();
	}

	@POST
	@Path("chat")
	public Response chat(String sJson) {
		return Response.ok("{\"msg\":\"" + tMenue.menueMsg(sJson).replaceAll("\n", "\\\\n") + "\"}").build();
	}

}
