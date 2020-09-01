package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.JasmartyHome;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;
import org.wipf.jasmarty.logic.jasmarty.extensions.Winamp;

/**
 * @author wipf
 *
 */
@Path("/debug")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DebugRest {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Winamp winamp;
	@Inject
	JasmartyHome jHome;

	@POST
	@GET
	@Path("/lcd/write/{x}/{y}/{str}")
	public Response cWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		lcdConnect.writeLineToCache(x, y, s.toCharArray());
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/lcd/refresh")
	public Response refreshDisplay() {
		lcdConnect.refreshDisplay();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/lcd/writeAscii/{int}")
	public Response writeAscii(@PathParam("int") Integer n) {
		lcdConnect.writeAscii(n);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/lcd/pos/{x}/{y}")
	public Response pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		lcdConnect.setCursor(x, y);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/lcd/cls")
	public Response cls() {
		lcdConnect.clearScreen();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/winamp/control/{s}/{s2}")
	public Response control(@PathParam("s") String s, @PathParam("s") String s2) throws Exception {
		winamp.control(s, s2);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("/winamp/info/{s}")
	public Response info(@PathParam("s") String s) throws Exception {
		return Response.ok("{\"info\":\"" + winamp.getInfos(s) + "\"}").build();
	}

	@POST
	@GET
	@Path("/jasmarty/restart")
	public Response startAgain() {
		try {
			jHome.jasmartyRestart();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}
}
