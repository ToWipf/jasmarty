package org.wipf.jasmarty.rest.mttt;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.mttt.MtttService;

/**
 * @author Wipf
 *
 */
@Path("mttt")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//@RolesAllowed("admin")
@PermitAll
@ApplicationScoped
public class MtttRest {

	@Inject
	MtttService mttt;

	@GET
	@Path("do")
	@Produces(MediaType.TEXT_PLAIN)
	public Response doMttt() {
		// wipfConfig.setConfParam("discord_id", sId);
		return Response.ok(mttt.getTestdata()).build();
		// return ;
	}

}
