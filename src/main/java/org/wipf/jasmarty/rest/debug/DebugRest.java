package org.wipf.jasmarty.rest.debug;

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
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.lcd.JasmartyHome;
import org.wipf.jasmarty.logic.lcd.Lcd12864;
import org.wipf.jasmarty.logic.lcd.Lcd12864Cache;
import org.wipf.jasmarty.logic.lcd.LcdConnect;
import org.wipf.jasmarty.logic.tasks.CronDaily;
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
	JasmartyHome jHome;
	@Inject
	Lcd12864 lcd12864;
	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	TAppGrafana tAppGrafana;
	@Inject
	Wipf wipf;
	@Inject
	CronDaily cronDaily;

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
	@Path("lcd/writeAscii/{int}")
	public Response writeAscii(@PathParam("int") Integer n) {
		lcdConnect.write(n);
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

	@POST
	@GET
	@Path("dailyTask")
	public Response dailyTask() {
		cronDaily.dailyTask();
		return Response.ok().build();
	}

}
