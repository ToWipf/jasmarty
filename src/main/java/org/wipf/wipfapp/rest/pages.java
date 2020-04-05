package org.wipf.wipfapp.rest;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.wipfapp.logic.jasmarty.PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("/pages")
public class pages {

	@Inject
	PageVerwaltung pageVerwaltung;

	@GET
	@PUT
	@Path("/new/{name}/{page}")
	@Produces(MediaType.TEXT_PLAIN)
	public String newPage(@PathParam("name") String sName, @PathParam("page") String sPage) {
		try {
			pageVerwaltung.newPageToDB(sName, sPage);
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
	@PUT
	@Path("/get/{pid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPage(@PathParam("pid") int nPid) {
		try {
			return pageVerwaltung.getPageFromDB(nPid).getPageAsDBString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "F";
	}

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
