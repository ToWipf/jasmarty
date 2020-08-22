package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.LcdConnect;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("/lcd")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	SerialConfig serialConfig;

	@GET
	@Path("/config/get")
	public Response getConfig() {
		return Response.ok(serialConfig.getConfig().toJson()).build();
	}

	@POST
	@Path("/config/set")
	public Response setConfig(String jnRoot) {
		return Response.ok("{\"save\":\"" + serialConfig.setConfig(jnRoot) + "\"}").build();

	}

	@GET
	@Path("/open")
	public Response open() {
		return Response.ok("{\"open\":\"" + lcdConnect.startSerialLcdPort() + "\"}").build();
	}

	@GET
	@Path("/close")
	public Response close() {
		return Response.ok("{\"close\":\"" + lcdConnect.closeSerialLcdPort() + "\"}").build();
	}

	@GET
	@Path("/ist")
	public Response chIst() {
		return Response.ok(lcdConnect.getCache().toIstJson().toString()).build();
	}

	@GET
	@Path("/soll")
	public Response chSoll() {
		return Response.ok(lcdConnect.getCache().toSollJson().toString()).build();
	}

}
