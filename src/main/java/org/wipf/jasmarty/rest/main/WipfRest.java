package org.wipf.jasmarty.rest.main;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.discord.DiscordHome;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

import io.quarkus.elytron.security.common.BcryptUtil;

/**
 * @author wipf
 *
 */
@Path("wipf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class WipfRest {

	@Inject
	SerialConfig serialConfig;
	@Inject
	DiscordHome discord;
	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;

	@POST
	@Path("setDiscordId/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response discordSetId(@PathParam("id") String sId) throws IOException, SQLException {
		wipfConfig.setConfParam("discord_id", sId);
		return Response.ok(wipfConfig.getConfParamString("discord_id")).build();
	}

	@GET
	@Path("up")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed({ "admin", "check", "user" })
	public Response up() {
		return Response.ok(1).build();
	}

	@GET
	@Path("ver")
	@PermitAll
	public Response getVer() {
		return Response.ok("{\"ver\":\"" + MainHome.VERSION + "\"}").build();
	}

	@GET
	@Path("time")
	public Response getTime() {
		return Response.ok("{\"time\":\"" + wipf.getTime() + "\"}").build();
	}

	@GET
	@Path("ports")
	public Response ports() {
		return Response.ok(serialConfig.getPorts().toString()).build();
	}

	@POST
	@Path("stop")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response stopAll() {
		MainHome.stopApp();
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("bct/{x}")
	public Response bct(@PathParam("x") String x) {
		return Response.ok(BcryptUtil.bcryptHash(x)).build();
	}

	@DELETE
	@Path("garbage")
	public void gc() {
		System.gc();
	}

	@GET
	@Path("/me")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String me(@Context SecurityContext securityContext) {
		return securityContext.getUserPrincipal().getName();
	}

	@GET
	@Path("/cotest")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String cotest(@CookieParam("fw") String c1) {
		return c1;
	}
}
