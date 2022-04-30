package org.wipf.jasmarty.rest;

import java.sql.SQLException;

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

import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author wipf
 *
 */
@Path("basesettings") // TODO anpassen
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfConfigRest {

	@Inject
	WipfConfig wipfConfig;

	@POST
	@Path("set/{appname}/{status}")
	public Response setConfig(@PathParam("appname") String sAppname, @PathParam("status") boolean bStatus) {
		try {
			wipfConfig.setAppStatus(sAppname, bStatus); // TODO
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok("{\"save\":\"" + "todo" + "\"}").build(); // TODO
	}

	@GET
	@Path("get/{appname}")
	public Response getConfig(@PathParam("appname") String sAppname) {
		try { // TODO
			return Response.ok("{\"active\":" + wipfConfig.isAppActive(sAppname) + "}").build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Path("getAll")
	public Response getConfig() {
		return Response.ok(wipfConfig.getAllAsJson().toString()).build();
	}

	@POST
	@Path("save")
	public Response saveTodo(String jnRoot) {
		return Response.ok("{\"save\":\"" + wipfConfig.saveItem(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") String sKey) {
		wipfConfig.deleteItem(sKey);
		return Response.ok().build();
	}

}
