package org.wipf.jasmarty.rest;

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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase;
import org.wipf.jasmarty.datatypes.wipfapp.PunktePlay;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfDebug;
import org.wipf.jasmarty.logic.discord.Discord;
import org.wipf.jasmarty.logic.jasmarty.JasmartyHome;
import org.wipf.jasmarty.logic.jasmarty.LcdConnect;
import org.wipf.jasmarty.logic.jasmarty.extensions.Winamp;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864Cache;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.Lcd2004;
import org.wipf.jasmarty.logic.telegram.TAppGrafana;

/**
 * @author wipf
 *
 */
@Path("debug")
@RolesAllowed("admin")
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
	@Inject
	Lcd2004 lcd2004;
	@Inject
	Lcd12864 lcd12864;
	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	TAppGrafana tAppGrafana;
	@Inject
	Wipf wipf;
	@Inject
	WipfDebug debug;
	@Inject
	Discord discord;

	@GET
	@PermitAll
	@Path("discord/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response discord(@PathParam("id") String sId) throws IOException {
		return Response.ok(discord.isOnline(sId)).build();
	}

	@POST
	@GET
	@Path("lcd12864/test2")
	public Response lcd12864test2() {
		lcd12864.setCacheRnd();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/re")
	public Response lcd12864testRe() {
		lcd12864.refreshDisplay();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/c")
	public Response lcd12864testC() {
		return Response.ok(lcd12864Cache.isChanged()).build();
	}

	@POST
	@GET
	@Path("lcd12864/t")
	public Response lcd12864testT() {
		lcd12864Cache.setChanged(true);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/writeFull")
	public Response lcd12864WriteFull(String sJson) {
		lcd12864Cache.setScreen(new Lcd12864PageBase(sJson));
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/testdf")
	public Response lcd12864testdf() {
		lcd12864.testDisplayFunctions();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/testt")
	public Response lcd12864testt() {
		lcd12864.testDisplayText();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/test57")
	public Response lcd12864test57() {
		lcd12864.testDisplayFont57();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd12864/test68")
	public Response lcd12864test68() {
		lcd12864.testDisplayFont68();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd/write/{x}/{y}/{str}")
	public Response cWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		lcd2004.writeLineToCache(x, y, s.toCharArray());
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd/refresh")
	public Response refreshDisplay() {
		lcd2004.refreshDisplay();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd/writeAscii/{int}")
	public Response writeAscii(@PathParam("int") Integer n) {
		lcdConnect.write(n);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd/pos/{x}/{y}")
	public Response pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		lcd2004.setCursor(x, y);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("lcd/cls")
	public Response cls() {
		lcd2004.clearScreen();
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("winamp/control/{s}/{s2}")
	public Response control(@PathParam("s") String s, @PathParam("s") String s2) throws Exception {
		winamp.control(s, s2);
		return Response.ok().build();
	}

	@POST
	@GET
	@Path("winamp/info/{s}")
	public Response info(@PathParam("s") String s) throws Exception {
		return Response.ok("{\"info\":\"" + winamp.getInfos(s) + "\"}").build();
	}

	@POST
	@GET
	@Path("jasmarty/restart")
	public Response startAgain() {
		try {
			jHome.jasmartyRestart();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@PUT
	@POST
	@DELETE
	@Path("test")
	public Response test() {
		return Response.ok("{\"test\": \"ok\"}").build();
	}

	@GET
	@PermitAll
	@Path("vergleiche/{a}")
	public Response vergleicheRND(@PathParam("a") String a) {
		PunktePlay pa = new PunktePlay(a);
		PunktePlay pb = new PunktePlay(String.valueOf(wipf.getRandomInt(Integer.MAX_VALUE)));

		return Response.ok("{\"test\": \"" + pa.vergleiche(pb) + "\"}").build();
	}

	@GET
	@PermitAll
	@Path("vergleiche/{a}/{b}")
	public Response vergleiche(@PathParam("a") String a, @PathParam("b") String b) {
		PunktePlay pa = new PunktePlay(a);
		PunktePlay pb = new PunktePlay(b);

		return Response.ok("{\"test\": \"" + pa.vergleiche(pb) + "\"}").build();
	}

	/**
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("lasttest")
	public Response lasttest() throws Exception {
		return Response.ok(debug.lasttestAsJson().toString()).build();
	}

}
