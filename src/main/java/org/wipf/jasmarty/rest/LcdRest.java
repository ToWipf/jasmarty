package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;

/**
 * @author wipf
 *
 */
@Path("/lcd")
@ApplicationScoped
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;

	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public Response open() {
		return Response.ok("{\"open\":\"" + lcdConnect.startPort() + "\"}").build();
	}

	@GET
	@Path("/close")
	@Produces(MediaType.APPLICATION_JSON)
	public Response close() {
		return Response.ok("{\"close\":\"" + lcdConnect.close() + "\"}").build();
	}

	@GET
	@Metered
	@Path("/ist")
	@Produces(MediaType.TEXT_PLAIN)
	public Response chIst() {
		return Response.ok(lcdConnect.getCache().toIstJson().toString()).build();
	}

	@GET
	@Path("/soll")
	@Produces(MediaType.TEXT_PLAIN)
	public Response chSoll() {
		return Response.ok(lcdConnect.getCache().toSollJson().toString()).build();
	}

}
