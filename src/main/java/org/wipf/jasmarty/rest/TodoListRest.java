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

import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

/**
 * @author wipf
 *
 */
@Path("/todolist")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TodoListRest {

	@Inject
	TAppTodoList todoList;

	@POST
	@Path("/saveTodo")
	public Response saveTodo(String jnRoot) {
		return Response.ok("{\"save\":\"" + todoList.setTodo(jnRoot) + "\"}").build();
	}

	@GET
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Integer nId) {
		todoList.delete(nId);
		return Response.ok().build();
	}

	@GET
	@Path("/getAll")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getall() {
		return Response.ok(todoList.getAll()).build();
	}

	@GET
	@Path("/getByUserID/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getByUserID(@PathParam("id") Integer nId) {
		return Response.ok(todoList.getAllByUser(nId)).build();
	}

	@GET
	@Path("/getAllFull")
	@Produces(MediaType.TEXT_PLAIN)
	public Response todolist() {
		return Response.ok(todoList.getAllFull()).build();
	}

	@GET
	@Path("/getAllJson")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllJson() {
		return Response.ok(todoList.getAllAsJson()).build();
	}
}
