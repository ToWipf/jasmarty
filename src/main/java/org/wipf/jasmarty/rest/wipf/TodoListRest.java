package org.wipf.jasmarty.rest.wipf;

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

import org.wipf.jasmarty.logic.telegram.TAppTodoList;

/**
 * @author wipf
 *
 */
@Path("todolist")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TodoListRest {

	@Inject
	TAppTodoList todoList;

	@POST
	@Path("saveTodo")
	public Response saveTodo(String jnRoot) {
		return Response.ok("{\"save\":\"" + todoList.saveItem(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) {
		todoList.deleteItem(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(todoList.getAllAsJson().toString()).build();
	}

}
