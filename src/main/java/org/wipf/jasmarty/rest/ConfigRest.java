package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ConfigRest {

	@Inject
	SerialConfig serialConfig;

	@GET
	@Path("/get")
	public Response getConfig() {
		return Response.ok(serialConfig.getConfig().toJson()).build();
	}

	@POST
	@Path("/set")
	public Response setConfig(String jnRoot) {
		return Response.ok("{\"save\":\"" + serialConfig.setConfig(jnRoot) + "\"}").build();

	}

}
