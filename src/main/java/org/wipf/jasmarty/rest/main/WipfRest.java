package org.wipf.jasmarty.rest.main;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.MainHome;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.base.WipfInfo;
import org.wipf.jasmarty.logic.discord.Discord;
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
	WipfInfo wipfInfo;
	@Inject
	Discord discord;
	@Inject
	WipfConfig wipfConfig;

	@GET
	@PermitAll
	@Path("discord/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response discord(@PathParam("id") String sId) throws IOException {
		return Response.ok(discord.isOnline(sId)).build();
	}

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
	@RolesAllowed({ "admin", "check, user" })
	public Response up() {
		return Response.ok(1).build();
	}

	@GET
	@Path("ver")
	@PermitAll
	public Response getver() {
		return Response.ok("{\"ver\":\"" + MainHome.VERSION + "\"}").build();
	}

	@GET
	@Path("ports")
	public Response ports() {
		return Response.ok(serialConfig.getPorts().toString()).build();
	}

	@GET
	@Path("tinfo")
	@Produces(MediaType.TEXT_PLAIN)
	@PermitAll
	public Response tinfo() {
		return Response.ok(wipfInfo.getThreadInfo()).build();
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
}
