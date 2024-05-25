package org.wipf.jasmarty.rest.telegram;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.TUsercache;

/**
 * @author wipf
 *
 */
@Path("teleusercache")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class TUsercacheRest {

	@Inject
	TUsercache tUsercache;

// Sollte nicht möglich sein, von außen
//	@POST
//	@Path("save")
//	public Response saveTodo(Usercache u) {
//		tUsercache.save(u);
//		return Response.ok("{}").build();
//	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Long nId) {
		tUsercache.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("getAll")
	public Response getall() {
		return Response.ok(tUsercache.getAll()).build();
	}

}
