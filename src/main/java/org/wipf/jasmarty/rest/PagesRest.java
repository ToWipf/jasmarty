package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.jasmarty.logic.jasmarty.PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/pages")
public class PagesRest {

	@Inject
	PageVerwaltung pageVerwaltung;

	@GET
	@PUT
	@Path("/new/{name}/{page}/{options}")
	@Produces(MediaType.TEXT_PLAIN)
	public String newPageOpt(@PathParam("name") String sName, @PathParam("page") String sPage,
			@PathParam("options") String sOptions) {
		try {
			pageVerwaltung.newPageToDB(sName, sPage, sOptions);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}

	@GET
	@PUT
	@Path("/select/{pid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String newPage(@PathParam("pid") int nPid) {
		try {
			pageVerwaltung.selectPage(nPid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
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
	public String save(String jnRoot) {
		pageVerwaltung.newPageToDB(jnRoot);
		return "{}";
	}

//	@GET
//	@PUT
//	@Path("/getRaw/{pid}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String getPageRaw(@PathParam("pid") int nPid) {
//		return pageVerwaltung.getPageFromDB(nPid).getPageAsDBString();
//	}

	@GET
	@DELETE
	@Path("/delete/{pid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@PathParam("pid") int nPid) {
		try {
			pageVerwaltung.delPageFromDB(nPid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}

}
