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

import org.wipf.jasmarty.logic.daylog.DaylogBoolEventDB;
import org.wipf.jasmarty.logic.daylog.DaylogEvent;
import org.wipf.jasmarty.logic.daylog.DaylogNumberEventDB;
import org.wipf.jasmarty.logic.daylog.DaylogTextEventDB;

/**
 * @author Wipf
 *
 */
@Path("daylog/event")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DaylogEventRest {

	@Inject
	DaylogEvent daylogEvent;
	@Inject
	DaylogTextEventDB daylogTextEventDB;
	@Inject
	DaylogBoolEventDB daylogBoolEventDB;
	@Inject
	DaylogNumberEventDB daylogNumberEventDB;

	@POST
	@Path("save")
	public Response save(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogEvent.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") Integer nId) throws SQLException {
		daylogEvent.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("get/{dateid}")
	public Response get(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogEvent.getAsJson(nDateid)).build();
	}

	/// Text

	@POST
	@Path("text/save")
	public Response textSave(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogTextEventDB.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("text/delete/{id}")
	public Response textDelete(@PathParam("id") Integer nId) throws SQLException {
		daylogTextEventDB.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("text/get/{dateid}")
	public Response textGet(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogTextEventDB.getAsJson(nDateid)).build();
	}

	/// Bool

	@POST
	@Path("bool/save")
	public Response boolSave(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogBoolEventDB.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("bool/delete/{id}")
	public Response boolDelete(@PathParam("id") Integer nId) throws SQLException {
		daylogBoolEventDB.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("bool/get/{dateid}")
	public Response boolGet(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogBoolEventDB.getAsJson(nDateid)).build();
	}

	/// Number

	@POST
	@Path("number/save")
	public Response numberSave(String jnRoot) throws SQLException {
		return Response.ok("{\"save\":\"" + daylogNumberEventDB.save(jnRoot) + "\"}").build();
	}

	@DELETE
	@Path("number/delete/{id}")
	public Response numberDelete(@PathParam("id") Integer nId) throws SQLException {
		daylogNumberEventDB.del(nId);
		return Response.ok().build();
	}

	@GET
	@Path("number/get/{dateid}")
	public Response numberGet(@PathParam("dateid") Integer nDateid) throws SQLException {
		return Response.ok(daylogNumberEventDB.getAsJson(nDateid)).build();
	}

}
