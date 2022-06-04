package org.wipf.jasmarty.rest.jasmarty;

import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.LcdConnect;
import org.wipf.jasmarty.logic.jasmarty.SerialConfig;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864Cache;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.Lcd2004;

/**
 * @author wipf
 *
 */
@Path("lcd")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Lcd2004 lcd2004;
	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	SerialConfig serialConfig;

	@GET
	@PermitAll
	@Path("config/get")
	public Response getConfig() {
		try {
			return Response.ok(serialConfig.getConfig().toJson()).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("config/set")
	public Response setConfig(String jnRoot) {
		try {
			serialConfig.setConfig(jnRoot);
			return Response.ok("{\"save\":\"" + "todo" + "\"}").build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@GET
	@Path("open")
	public Response open() {
		return Response.ok("{\"open\":\"" + lcdConnect.startSerialLcdPort() + "\"}").build();
	}

	@GET
	@Path("close")
	public Response close() {
		return Response.ok("{\"close\":\"" + lcdConnect.closeSerialLcdPort() + "\"}").build();
	}

	// ############################
	// ########### 2004 ###########
	// ############################

	@GET
	@Path("ist")
	public Response chIst() {
		return Response.ok(lcd2004.getCache().toIstJson().toString()).build();
	}

	@GET
	@Path("soll")
	public Response chSoll() {
		return Response.ok(lcd2004.getCache().toSollJson().toString()).build();
	}

}
