package org.wipf.jasmarty.rest.base;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.base.WipfConfig;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

/**
 * @author wipf
 *
 */
@Path("basesettings")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfConfigRest {

	@Inject
	WipfConfigVW wipfConfig;

	@GET
	@PermitAll
	@Path("get/{appname}")
	public Response getConfig(@PathParam("appname") String sAppname) {
		return Response.ok("{\"active\":" + wipfConfig.isAppActive(sAppname) + "}").build();
	}

	@GET
	@Path("getAll")
	public Response getConfig() {
		return Response.ok(wipfConfig.getAll()).build();
	}

	@POST
	@Path("save")
	public Response saveItem(WipfConfig wu) {
		wipfConfig.saveItem(wu);
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") String sKey) {
		wipfConfig.deleteItem(sKey);
		return Response.ok().build();
	}

}
