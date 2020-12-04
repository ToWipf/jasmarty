package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.BaseSettings;

/**
 * @author wipf
 *
 */
@Path("/basesettings")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BaseSettingsRest {

	@Inject
	BaseSettings baseSettings;

	@POST
	@Path("/set/{appname}/{status}")
	public Response setConfig(@PathParam("appname") String sAppname, @PathParam("status") boolean bStatus) {
		try {
			baseSettings.setAppStatus(sAppname, bStatus); // TODO
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok("{\"save\":\"" + "todo" + "\"}").build(); // TODO
	}

	@GET
	@Path("/get/{appname}")
	public Response getConfig(@PathParam("appname") String sAppname) {
		try { // TODO
			return Response.ok("{\"active\":" + baseSettings.isAppActive(sAppname) + "}").build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

}
