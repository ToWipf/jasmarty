package org.wipf.wipfapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.wipfapp.logic.base.MBlowfish;
import org.wipf.wipfapp.logic.base.MWipf;
import org.wipf.wipfapp.logic.base.Wipfapp;
import org.wipf.wipfapp.logic.telegram.apps.MOthers;

/**
 * @author wipf
 *
 */
@Path("/wipf")
public class RestWipf {

	@GET
	@Path("/ping/{ip}")
	@Produces(MediaType.TEXT_PLAIN)
	public String ping(@PathParam("ip") String sIP) {
		return MWipf.ping(sIP).toString();
	}

	@GET
	@Path("/date")
	@Produces(MediaType.TEXT_PLAIN)
	public Response date() {
		return MWipf.genResponse(MWipf.date());
	}

	@GET
	@Path("/r/{bis}/{anzahl}")
	@Produces(MediaType.TEXT_PLAIN)
	public String zufall(@PathParam("bis") Integer nBis, @PathParam("anzahl") Integer nAnzahl) {
		return MOthers.zufall(nBis, nAnzahl);
	}

	// Blowfish
	@GET
	@Path("/cr/{txt}")
	@Produces(MediaType.TEXT_PLAIN)
	public String cr(@PathParam("txt") String sIn) throws Exception {
		return MBlowfish.encrypt(sIn, Wipfapp.sKey);
	}

	@GET
	@Path("/dc/{txt}")
	@Produces(MediaType.TEXT_PLAIN)
	public String dc(@PathParam("txt") String sIn) throws Exception {
		return MBlowfish.decrypt(sIn, Wipfapp.sKey);
	}

	@PUT
	@Path("/gc")
	@Produces(MediaType.TEXT_PLAIN)
	public String gc() throws Exception {
		MWipf.runGc();
		return "get";
	}

	@GET
	@Path("/ver")
	@Produces(MediaType.APPLICATION_JSON)
	public String getver() {
		return "{\"ver\":\"" + Wipfapp.VERSION + "\"}";
	}

	// System
//	@DELETE
//	@Path("/sysHalt")
//	public void sysHalt() {
//		LOGGER.info("SysHalt");
//		System.exit(0);
//	}

}
