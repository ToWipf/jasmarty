package org.wipf.jasmarty.rest.wipf;

import java.sql.SQLException;

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
import org.wipf.jasmarty.databasetypes.telegram.Usercache;
import org.wipf.jasmarty.logic.telegram.TAppMsg;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;
import org.wipf.jasmarty.logic.telegram.TUsercache;
import org.wipf.jasmarty.logic.telegram.TeleLog;
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
	TeleLog tlog;
	@Inject
	TeleMenue tMenue;
	@Inject
	TAppMsg tAppMsg;
	@Inject
	TUsercache tUsercache;

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
			return Response.ok("{\"botkey\":\"" + tVerwaltung.getBotKey() + "\"}").build();
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
		// TODO:
		return Response.ok("{}").build();
	}

	@POST
	@PermitAll
	@Path("sendMsgToAdmin/{msg}")
	public Response sendMsgToId(@PathParam("msg") String sMsg) {
		tVerwaltung.sendMsgToAdmin(sMsg);
		// TODO:
		return Response.ok("{}").build();
	}

	@GET
	@Path("log")
	public Response log() {
		return Response.ok(tlog.getTelegramLogAsJson().toString()).build();
	}

	@GET
	@Path("logext")
	public Response logs() {
		return Response.ok(tlog.getTelegramLogAsJsonEXTERN().toString()).build();
	}

	@DELETE
	@Path("delLog/{id}")
	public Response delLog(@PathParam("id") Long nId) {
		return Response.ok("{\"del\":\"" + tlog.delItem(nId) + "\"}").build();
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

	@GET
	@Path("msgall")
	public Response msg() {
		return Response.ok(tAppMsg.getMsgAsJson().toString()).build();
	}

	@POST
	@Path("saveMsg")
	public Response saveMsg(String sJson) {
		return Response.ok("{\"save\":\"" + tAppMsg.saveMsg(sJson) + "\"}").build();
	}

	@DELETE
	@Path("delMsg/{id}")
	public Response delMsg(@PathParam("id") Integer nId) {
		return Response.ok("{\"del\":\"" + tAppMsg.delItem(nId) + "\"}").build();
	}

	// Usercache

	@POST
	@Path("usercache/save")
	public Response saveTodo(Usercache u) throws SQLException {
		tUsercache.save(u);
		return Response.ok("{}").build();
	}

	@DELETE
	@Path("usercache/delete/{id}")
	public Response delete(@PathParam("id") Long nId) throws SQLException {
		tUsercache.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("usercache/getAll")
	public Response getall() {
		return Response.ok(tUsercache.getAll()).build();
	}

}
