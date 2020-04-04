package org.wipf.wipfapp.rest;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.GET;
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
	@Path("/write/{x}/{y}/{str}")
	@Produces(MediaType.TEXT_PLAIN)
	public String newPage(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		try {
			pageVerwaltung.newPageToDB(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}

}
