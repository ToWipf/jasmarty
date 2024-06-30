package org.wipf.jasmarty.rest.main;

import java.io.IOException;

import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.discord.DiscordHome;
import org.wipf.jasmarty.logic.lcd.SerialConfig;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author wipf
 *
 */
@Path("wipf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@RequestScoped
public class WipfRest {

	@Inject
	SerialConfig serialConfig;
	@Inject
	DiscordHome discord;
	@Inject
	Wipf wipf;
	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	AuthKeyService authKeyService;

	@POST
	@Path("setDiscordId/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response discordSetId(@PathParam("id") String sId) throws IOException {
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
	public Boolean cotest(@CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		return (authKeyService.isKeyInCache(key));
	}
}
