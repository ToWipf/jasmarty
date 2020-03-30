package org.wipf.wipfapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.wipfapp.logic.base.MWipf;
import org.wipf.wipfapp.logic.telegram.system.MTeleMsg;
import org.wipf.wipfapp.logic.telegram.system.MTelegram;

/**
 * @author wipf
 *
 */
@Path("/telegram")
public class RestTelegram {

	@POST
	@Path("/setbot/{bot}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response setbot(@PathParam("bot") String sBot) {
		return MWipf.genResponse(MTelegram.setbot(sBot));
	}

	@POST
	@Path("/sendMsgToGroup/{msg}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sendMsgToGroup(@PathParam("msg") String sMsg) {
		return MWipf.genResponse(MTeleMsg.sendMsgToGroup(sMsg));
	}

	@GET
	@Path("/telelog")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelog() {
		return MWipf.genResponse(MTelegram.getTelegramLog(null));
	}

	@GET
	@Path("/telelogtf")
	@Produces(MediaType.TEXT_PLAIN)
	public Response telelogtf() {
		return MWipf.genResponse(MTelegram.getTelegramLog("798200105"));
	}

}
