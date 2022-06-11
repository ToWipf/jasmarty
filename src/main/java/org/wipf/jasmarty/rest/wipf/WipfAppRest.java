package org.wipf.jasmarty.rest.wipf;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.wipfapp.AppInfos;
import org.wipf.jasmarty.logic.wipfapp.Infotext;
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
	@Inject
	Infotext infotext;

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

//	@POST
//	@Path("setPunkte/{p}")
//	public Response setPunkte(@PathParam("p") Integer nP) {
//		punkteVW.setPunkte(nP);
//		return Response.ok().build();
//	}

	@POST
	@Path("playPunkte")
	public Response setPunkte(String sJson) {
		return Response.ok("{\"text\":\"" + punkteVW.playPunkte(sJson) + "\"}").build();
	}

	@GET
	@Path("getPunkte")
	public Response getPunkte() {
		return Response.ok("{\"punkte\":\"" + punkteVW.getPunkte() + "\"}").build();
	}

	@GET
	@Path("getNochSpiele")
	public Response getNochSpiele() {
		return Response.ok("{\"nochspiele\":\"" + punkteVW.getNochSpielen() + "\"}").build();
	}

	@GET
	@POST
	@Path("setInfotext/s}")
	public Response setInfotext(@PathParam("s") String s) {
		infotext.setText(s);
		return Response.ok().build();
	}

}
