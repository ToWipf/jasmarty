package org.wipf.jasmarty.rest.telegram;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.TUsercache;

/**
 * @author wipf
 *
 */
@Path("teleusercache")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
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
