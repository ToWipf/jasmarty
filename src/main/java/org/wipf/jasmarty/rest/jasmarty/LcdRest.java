package org.wipf.jasmarty.rest.jasmarty;

import org.wipf.jasmarty.logic.lcd.Lcd12864Cache;
import org.wipf.jasmarty.logic.lcd.LcdConnect;
import org.wipf.jasmarty.logic.lcd.SerialConfig;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author wipf
 *
 */
@Path("lcd")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	SerialConfig serialConfig;

	@GET
	@PermitAll
	@Path("config/get")
	public Response getConfig() {
		return Response.ok(serialConfig.getConfig().toJson()).build();
	}

	@POST
	@Path("config/set")
	public Response setConfig(String jnRoot) {
		serialConfig.setConfig(jnRoot);
		return Response.ok("{}").build();
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

}
