package org.wipf.jasmarty.rest;

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

import org.wipf.jasmarty.logic.jasmarty.PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/pages")
@ApplicationScoped
public class PagesRest {

	@Inject
	PageVerwaltung pageVerwaltung;

	@GET
	@PUT
	@Path("/new/{name}/{page}/{options}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newPageOpt(@PathParam("name") String sName, @PathParam("page") String sPage,
			@PathParam("options") String sOptions) {
		pageVerwaltung.newPageToDB(sName, sPage, sOptions);
		return Response.ok("{}").build();
	}

	@GET
	@PUT
	@Path("/select/{pid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response newPage(@PathParam("pid") int nPid) {
		pageVerwaltung.selectPage(nPid);
		return Response.ok("{}").build();
	}

	@GET
	@Path("/get/{pid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPage(@PathParam("pid") int nPid) {
		return pageVerwaltung.getPageFromDB(nPid).toJson();
	}

	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(String jnRoot) {
		pageVerwaltung.newPageToDB(jnRoot);
		return Response.ok("{}").build();
	}

	@GET
	@DELETE
	@Path("/delete/{pid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("pid") int nPid) {
		pageVerwaltung.delPageFromDB(nPid);
		return Response.ok("{}").build();
	}

}
