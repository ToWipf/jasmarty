package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.PageConverter;
import org.wipf.jasmarty.logic.jasmarty.lcd2004.PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("pages")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON) TODO POST DELETE geht nicht
@ApplicationScoped
public class PagesRest {

	@Inject
	PageVerwaltung pageVerwaltung;
	@Inject
	PageConverter pageConverter;

	@GET
	@PUT
	@Path("select/{pid}")
	public Response newPage(@PathParam("pid") int nPid) {
		pageVerwaltung.selectPage(nPid);
		return Response.ok().build();
	}

	@GET
	@Path("get/{pid}")
	public Response getPage(@PathParam("pid") int nPid) {
		return Response.ok(pageVerwaltung.getPageFromDb(nPid).toJson()).build();
	}

	@GET
	@Path("current")
	public Response getCurrent() {
		return Response.ok(pageConverter.getCurrentSite().toJson()).build();
	}

	@POST
	@Path("save")
	public Response save(String jnRoot) {
		try {
			pageVerwaltung.pageToDb(jnRoot);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("delete/{pid}")
	public Response delete(@PathParam("pid") int nPid) {
		try {
			pageVerwaltung.delPageFromDb(nPid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@Path("getAllPages")
	public Response getAllPages() {
		try {
			return Response.ok(pageVerwaltung.getAllPages().toString()).build();
		} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
