package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.daylog.DaylogTextEventDB;

/**
 * @author Wipf
 *
 */
@Path("daylog/textevent")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DaylogTextEventRest {

	@Inject
	DaylogTextEventDB daylogTextEventDB;

	@POST
	@Path("save")
	public Response save(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogTextEventDB.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogTextEventDB.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("get/{dateid}")
	public Response get(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogTextEventDB.getAsJson(nDateid)).build();
	}

}
