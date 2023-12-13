package org.wipf.jasmarty.rest.wipf;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.wipf.jasmarty.logic.wipfapp.AppInfos;
import org.wipf.jasmarty.logic.wipfapp.PunkteVW;

/**
 * @author wipf
 *
 */
@Path("wipfapp")
@RolesAllowed({ "admin", "user" })
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WipfAppRest {

	@Inject
	AppInfos appInfos;
	@Inject
	PunkteVW punkteVW;

	@GET
	@Path("starttext")
	public Response starttext() {
		return Response.ok("{\"data\":\"" + appInfos.genStarttext() + "\"}").build();
	}

	@GET
	@Path("infotext")
	public Response infotext() {
		return Response.ok("{\"data\":\"" + appInfos.genInfotext() + "\"}").build();
	}

	@POST
	@Path("playPunkte")
	public Response setPunkte(String sJson) {
		return Response.ok("{\"text\":\"EOL\"}").build();
	}

	@GET
	@Path("getPunkte")
	public Response getPunkte() {
		return Response.ok("{\"punkte\":\"" + punkteVW.getPunkte() + "\"}").build();
	}

	@GET
	@POST
	@Path("setInfotext/s}")
	public Response setInfotext(@PathParam("s") String s) {
		appInfos.setText(s);
		return Response.ok().build();
	}

}
