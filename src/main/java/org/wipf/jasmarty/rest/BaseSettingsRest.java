package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
		return Response.ok("{\"save\":\"" + baseSettings.setAppStatus(sAppname, bStatus) + "\"}").build();
	}

}
