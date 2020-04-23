package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
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
@ApplicationScoped
public class ConfigRest {

	@Inject
	SerialConfig serialConfig;

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConfig() {
		return Response.ok(serialConfig.getConfig().toJson()).build();
	}

	@POST
	@Path("/set")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setConfig(String jnRoot) {
		return Response.ok("{\"save\":\"" + serialConfig.setConfig(jnRoot) + "\"}").build();

	}

	@OPTIONS
	@Path("/set")
	public Response setOptions() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
	}

}
