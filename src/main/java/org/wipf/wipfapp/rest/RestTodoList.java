package org.wipf.wipfapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.wipfapp.logic.base.MWipf;
import org.wipf.wipfapp.logic.telegram.apps.MTodoList;

/**
 * @author wipf
 *
 */
@Path("/todolist")
public class RestTodoList {

	@GET
	@Path("/getAll")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getall(@PathParam("bot") String sBot) {
		return MWipf.genResponse(MTodoList.getAll());
	}

	@GET
	@Path("/getByUserID/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getByUserID(@PathParam("id") Integer nId) {
		return MWipf.genResponse(MTodoList.getAllByUser(nId));
	}

	@GET
	@Path("/getAllFull")
	@Produces(MediaType.TEXT_PLAIN)
	public Response todolist() {
		return MWipf.genResponse(MTodoList.getAllFull());
	}

	@GET
	@Path("/getAllJson")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllJson() {
		return MWipf.genResponse(MTodoList.getAllAsJson());
	}

//	
//	// TODO
//	@POST
//	@Path("/addTodo/{msg}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response addTodo(@PathParam("msg") String sMsg) {
//		return MWipf.genResponse(MTeleMsg.sendMsgToGroup(sMsg));
//	}

}
