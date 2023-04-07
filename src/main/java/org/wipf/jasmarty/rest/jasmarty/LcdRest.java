package org.wipf.jasmarty.rest.jasmarty;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.lcd.Lcd12864Cache;
import org.wipf.jasmarty.logic.lcd.LcdConnect;
import org.wipf.jasmarty.logic.lcd.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("lcd")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
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
