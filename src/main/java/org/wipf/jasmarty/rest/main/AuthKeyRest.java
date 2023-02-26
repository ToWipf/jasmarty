package org.wipf.jasmarty.rest.main;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.databasetypes.base.AuthKey;

/**
 * @author wipf
 *
 */
@Path("authkey")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class AuthKeyRest {

//
//	@POST
//	@Path("setDiscordId/{id}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response discordSetId(@PathParam("id") String sId) throws IOException, SQLException {
//		wipfConfig.setConfParam("discord_id", sId);
//		return Response.ok(wipfConfig.getConfParamString("discord_id")).build();
//	}

//	@GET
//	@Path("up")
//	@Produces(MediaType.TEXT_PLAIN)
//	@RolesAllowed({ "admin", "check", "user" })
//	public Response up() {
//		return Response.ok(1).build();
//	}

	@GET
	@Path("do")
	@PermitAll
	@Transactional
	@Produces(MediaType.TEXT_PLAIN)
	public Response doit() {

		AuthKey a = new AuthKey();
		a.key = "42";
		a.persist();

		return Response.ok(a.toString()).build();
	}

}
