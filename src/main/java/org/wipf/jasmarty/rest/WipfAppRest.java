package org.wipf.jasmarty.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.wipfapp.AppInfos;

/**
 * @author wipf
 *
 */
@Path("wipfapp")
@RolesAllowed("admin")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class WipfAppRest {

	@Inject
	AppInfos appInfos;

	@GET
	@Path("starttext")
	public Response starttext() {
		System.out.println("starttext");
		return Response.ok(appInfos.genStarttext()).build();
	}

	@GET
	@Path("infotext")
	public Response infotext() {
		System.out.println("infotext");
		return Response.ok(appInfos.genInfotext()).build();
	}

}
