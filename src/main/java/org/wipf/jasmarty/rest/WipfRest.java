package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.QMain;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("/wipf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfRest {

	@Inject
	SerialConfig serialConfig;
	@Inject
	QMain qMain;

	@GET
	@Path("/ver")
	public Response getver() {
		return Response.ok("{\"ver\":\"" + QMain.VERSION + "\"}").build();
	}

	@GET
	@Path("/ports")
	public Response ports() {
		return Response.ok(serialConfig.getPorts().toString()).build();
	}

	@GET
	@Path("/startAgain")
	public Response startAgain() {
		qMain.startAgain();
		return Response.ok().build();
	}

	@GET
	@Path("/stop")
	public Response stopAll() {
		QMain.stopApp();
		return Response.ok().build();
	}

}
