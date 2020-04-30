package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;

	@GET
	@Path("/open")
	public Response open() {
		return Response.ok("{\"open\":\"" + lcdConnect.startPort() + "\"}").build();
	}

	@GET
	@Path("/close")
	public Response close() {
		return Response.ok("{\"close\":\"" + lcdConnect.close() + "\"}").build();
	}

	@GET
	@Path("/ist")
	public Response chIst() {
		return Response.ok(lcdConnect.getCache().toIstJson().toString()).build();
	}

	@GET
	@Metered
	@Path("/soll")
	public Response chSoll() {
		return Response.ok(lcdConnect.getCache().toSollJson().toString()).build();
	}

}
